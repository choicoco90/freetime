package org.example.freetime.repository

import org.example.freetime.entities.DailyFreeTimeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface DailyFreeTimeRepository: JpaRepository<DailyFreeTimeEntity,Long> {

    fun findByUserIdAndDate(userId: Long, date: LocalDate): DailyFreeTimeEntity?

    fun findAllByUserIdAndDateBetween(userId: Long, start: LocalDate, end: LocalDate): List<DailyFreeTimeEntity>
}
