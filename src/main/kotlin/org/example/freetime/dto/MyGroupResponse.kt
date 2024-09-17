package org.example.freetime.dto

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.UserEntity

data class MyGroupResponse(
    val groupId: Long,
    val groupName: String,
    val groupLeader: UserResponse,
    val description: String,
){
    companion object {
        fun from(entity: GroupEntity, leader: UserEntity ): MyGroupResponse {
            return MyGroupResponse(
                groupId = entity.id,
                groupName = entity.groupName,
                groupLeader = UserResponse.from(leader),
                description = entity.description,
            )
        }
    }
}
