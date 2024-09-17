package org.example.freetime.repository

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.GroupMeetingEntity
import org.example.freetime.entities.GroupUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupMeetingRepository: JpaRepository<GroupMeetingEntity, Long> {
    fun deleteAllByGroupId(groupId: Long)
}
