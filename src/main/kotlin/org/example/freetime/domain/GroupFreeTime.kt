package org.example.freetime.domain

import org.example.freetime.model.Schedule
import java.time.LocalDate

data class GroupFreeTime(
    val freeTimes: Map<LocalDate, List<Schedule>>
)
