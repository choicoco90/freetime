package org.example.freetime.service

import org.example.freetime.domain.GroupMeeting
import org.example.freetime.dto.request.GroupMeetingCreateRequest
import org.example.freetime.entities.GroupMeetingEntity
import org.example.freetime.enums.MeetingStatus
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import org.example.freetime.repository.GroupMeetingRepository
import org.example.freetime.repository.GroupRepository
import org.example.freetime.repository.GroupUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

// TODO: 여기에 이메일 보내는 기능을 추가
@Service
class GroupMeetingService(
    val groupMeetingRepository: GroupMeetingRepository,
    val groupRepository: GroupRepository,
    val groupUserRepository: GroupUserRepository
) {
    @Transactional(readOnly = false)
    fun createGroupMeeting(requesterId: Long, request: GroupMeetingCreateRequest, groupId: Long) {
        val group = groupRepository.findById(groupId).orElseThrow { throw BizException(ErrorCode.GROUP_NOT_FOUND) }
        if (group.groupLeader != requesterId) throw BizException(ErrorCode.NOT_GROUP_LEADER)
        val meeting = GroupMeetingEntity(
            groupId = groupId,
            start = request.start,
            end = request.end,
            place = request.place,
            description = request.description
        )
        groupMeetingRepository.save(meeting)
    }

    @Transactional(readOnly = false)
    fun cancelGroupMeeting(meetingId: Long, userId: Long) {
        val meeting = groupMeetingRepository
            .findById(meetingId)
            .orElseThrow { throw BizException(ErrorCode.MEETING_NOT_FOUND) }
        val group = groupRepository.findById(meeting.groupId).orElseThrow { throw BizException(ErrorCode.GROUP_NOT_FOUND) }
        if (group.groupLeader != userId) throw BizException(ErrorCode.NOT_GROUP_LEADER)
        meeting.status = MeetingStatus.CANCELED
        groupMeetingRepository.save(meeting)
    }

    @Transactional(readOnly = true)
    fun getGroupMeetingsOfPeriod(userId: Long, start: LocalDateTime, end: LocalDateTime): List<GroupMeeting> {
        val userGroups = groupUserRepository.findAllByUserId(userId)
        val groupIds = userGroups.map { it.groupId }
        val groups = groupRepository.findAllByIdIn(groupIds)
        val meetings = groupMeetingRepository.findAllByGroupIdInAndStartBetween(groupIds, start, end)
        return meetings.map {
            val group = groups.find { group -> group.id == it.groupId } ?: throw BizException(ErrorCode.GROUP_NOT_FOUND)
            GroupMeeting(it, group)
        }
    }

    @Transactional(readOnly = true)
    fun getGroupUsers(groupId: Long): List<Long> {
        return groupUserRepository.findAllByGroupId(groupId).map { it.userId }
    }

}
