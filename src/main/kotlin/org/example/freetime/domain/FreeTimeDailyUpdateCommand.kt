package org.example.freetime.domain

import org.example.freetime.entities.DailyFreeTimeEntity
import org.example.freetime.model.FreeTimes
import java.time.LocalDate

data class FreeTimeDailyUpdateCommand(
    val date: LocalDate,
    val freeTime: FreeTimes
){
    fun toEntity(userId: Long): DailyFreeTimeEntity {
        return DailyFreeTimeEntity(
            userId = userId,
            date = date,
            freeTime = freeTime.times
        )
    }
}
