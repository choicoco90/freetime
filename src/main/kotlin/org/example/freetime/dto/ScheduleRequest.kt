package org.example.freetime.dto

import org.example.freetime.domain.Schedule
import java.time.LocalDateTime

data class ScheduleRequest(
    val start: LocalDateTime,
    val end: LocalDateTime
){
    fun toDomain() = Schedule(start, end)
}
