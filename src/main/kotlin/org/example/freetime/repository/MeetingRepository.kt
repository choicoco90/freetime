package org.example.freetime.repository

import org.example.freetime.entities.MeetingEntity
import org.example.freetime.enum.MeetingStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface MeetingRepository: JpaRepository<MeetingEntity, Long> {

    fun findAllByUserIdAndStartBetween(userId: Long, from: LocalDateTime, to: LocalDateTime): List<MeetingEntity>

    fun findAllByUserIdAndStartBetweenAndStatus(userId: Long, from: LocalDateTime, to: LocalDateTime, status: MeetingStatus): List<MeetingEntity>

}
