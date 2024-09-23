package org.example.freetime.service

import org.example.freetime.dto.request.CreateGroupRequest
import org.example.freetime.dto.response.GroupResponse
import org.example.freetime.dto.response.MyGroupResponse
import org.example.freetime.dto.request.UpdateGroupRequest
import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.GroupUserEntity
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import org.example.freetime.repository.GroupMeetingRepository
import org.example.freetime.repository.GroupRepository
import org.example.freetime.repository.GroupUserRepository
import org.example.freetime.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class GroupService(
    val groupRepository: GroupRepository,
    val groupUserRepository: GroupUserRepository,
    val groupMeetingRepository: GroupMeetingRepository,
    val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getMyGroups(requesterId: Long): List<MyGroupResponse> {
        val groupUserEntities = groupUserRepository.findAllByUserId(requesterId)
        val groupIds = groupUserEntities.map { it.groupId }
        val groupEntities = groupRepository.findAllByIdIn(groupIds)
        val groupLeaderIds = groupEntities.map { it.groupLeader }
        val groupLeaders = userRepository.findAllByIdIn(groupLeaderIds).associateBy { it.id }
        val groupResponses = groupEntities.map { groupEntity ->
            val groupLeader = groupLeaders[groupEntity.groupLeader] ?: throw BizException(ErrorCode.USER_NOT_FOUND)
            MyGroupResponse.from(groupEntity, groupLeader)
        }
        return groupResponses
    }

    @Transactional(readOnly = true)
    fun getGroupInfo(groupId: Long): GroupResponse {
        val groupEntity = groupRepository.findById(groupId).getOrNull() ?: throw BizException(ErrorCode.GROUP_NOT_FOUND)
        val groupUsers = groupUserRepository.findAllByGroupId(groupId).mapNotNull {
            userRepository.findById(it.userId).getOrNull()
        }
        return GroupResponse.from(groupEntity, groupUsers)
    }

    @Transactional(readOnly = false)
    fun createGroup(createGroupRequest: CreateGroupRequest, requesterId: Long): GroupResponse {
        val groupEntity = GroupEntity(
            groupName = createGroupRequest.groupName,
            groupLeader = requesterId,
            description = createGroupRequest.description
        )
        val group = groupRepository.save(groupEntity)
        createGroupRequest.userIds.forEach { userId ->
            val groupUserEntity = GroupUserEntity(
                groupId = group.id,
                userId = userId
            )
            groupUserRepository.save(groupUserEntity)
        }
        return getGroupInfo(group.id)
    }

    @Transactional(readOnly = false)
    fun deleteMemberFromGroup(groupId: Long, userId: Long, requesterId: Long) {
        val groupEntity = groupRepository.findById(groupId).getOrNull() ?: throw BizException(ErrorCode.GROUP_NOT_FOUND)
        validateGroupLeader(groupEntity, requesterId)
        validateDeleteGroupLeader(groupEntity, userId)
        groupUserRepository.deleteByGroupIdAndUserId(groupId, userId)
    }

    @Transactional(readOnly = false)
    fun addMemberToGroup(groupId: Long, userId: Long, requesterId: Long) {
        val groupEntity = getGroup(groupId)
        validateGroupLeader(groupEntity, requesterId)
        validateAddMember(groupId, userId)
        val groupUserEntity = GroupUserEntity(
            groupId = groupId,
            userId = userId
        )
        groupUserRepository.save(groupUserEntity)
    }


    @Transactional(readOnly = false)
    fun updateGroup(request: UpdateGroupRequest, groupId: Long, requesterId: Long) {
        val groupEntity = getGroup(groupId)
        validateGroupLeader(groupEntity, requesterId)
        groupEntity.update(request)
        groupRepository.save(groupEntity)
    }

    @Transactional(readOnly = false)
    fun deleteGroup(groupId: Long, requesterId: Long) {
        val groupEntity = getGroup(groupId)
        validateGroupLeader(groupEntity, requesterId)
        groupRepository.deleteById(groupId)
        groupUserRepository.deleteAllByGroupId(groupId)
        groupMeetingRepository.deleteAllByGroupId(groupId)
    }


    private fun getGroup(groupId: Long): GroupEntity {
        return groupRepository.findById(groupId).getOrNull() ?: throw BizException(ErrorCode.GROUP_NOT_FOUND)
    }

    private fun validateGroupLeader(groupEntity: GroupEntity, userId: Long) {
        if (groupEntity.groupLeader != userId) {
            throw BizException(ErrorCode.NOT_GROUP_LEADER)
        }
    }

    private fun validateAddMember(groupId: Long, userId: Long) {
        val alreadyExists = groupUserRepository.existsByGroupIdAndUserId(groupId, userId)
        if (alreadyExists) {
            throw BizException(ErrorCode.ALREADY_GROUP_MEMBER)
        }
    }

    private fun validateDeleteGroupLeader(groupEntity: GroupEntity, userId: Long) {
        if (groupEntity.groupLeader == userId) {
            throw BizException(ErrorCode.CANNOT_DELETE_GROUP_LEADER)
        }
    }
}
