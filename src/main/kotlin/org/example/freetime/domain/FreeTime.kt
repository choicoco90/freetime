package org.example.freetime.domain

import java.time.LocalDate

data class FreeTime(
    override val startHour: Int,
    override val startMinute: Int,
    override val endHour: Int,
    override val endMinute: Int
): Time{
    fun toSchedule(date: LocalDate): Schedule {
        return Schedule(
            start = date.atTime(startHour, startMinute),
            end = date.atTime(endHour, endMinute)
        )
    }
}
