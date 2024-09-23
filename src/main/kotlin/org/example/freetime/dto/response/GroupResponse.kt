package org.example.freetime.dto.response

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.UserEntity

data class GroupResponse(
    val groupId: Long,
    val groupName: String,
    val groupLeader: UserResponse,
    val description: String,
    val groupMembers: List<UserResponse> = emptyList(),
){
    companion object {
        fun from(entity: GroupEntity, members: List<UserEntity> ): GroupResponse {
            val leader = members.find { it.id == entity.groupLeader } ?: throw IllegalStateException("그룹 리더를 찾을 수 없습니다.")
            val groupMembers = members.filter { it.id != entity.groupLeader }
            return GroupResponse(
                groupId = entity.id,
                groupName = entity.groupName,
                groupLeader = UserResponse.from(leader),
                description = entity.description,
                groupMembers = groupMembers.map { UserResponse.from(it) }
            )
        }
    }
}
