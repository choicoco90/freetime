package org.example.freetime.repository

import org.example.freetime.entities.MeetingEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface MeetingRepository: JpaRepository<MeetingEntity, Long> {

    fun findAllByUserIdAndStartBetween(userId: Long, from: LocalDateTime, to: LocalDateTime): List<MeetingEntity>

    fun findAllByRequesterIdAndStartBetween(userId: Long, from: LocalDateTime, to: LocalDateTime): List<MeetingEntity>

}
