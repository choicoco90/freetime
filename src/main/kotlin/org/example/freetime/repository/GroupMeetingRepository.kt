package org.example.freetime.repository

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.GroupMeetingEntity
import org.example.freetime.entities.GroupUserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.time.LocalDateTime

interface GroupMeetingRepository: JpaRepository<GroupMeetingEntity, Long> {
    fun deleteAllByGroupId(groupId: Long)

    fun findAllByGroupIdInAndStartBetween(groupIds: List<Long>, start: LocalDateTime, end: LocalDateTime): List<GroupMeetingEntity>
}
