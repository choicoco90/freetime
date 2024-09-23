package org.example.freetime.dto.response

import org.example.freetime.domain.Schedules
import java.time.LocalDate

data class WeeklyScheduleInfoResponse(
    val schedules: Map<LocalDate, Schedules>,
    val ownedMeetings: List<MeetingResponse>,
    val guestMeetings: List<MeetingResponse>
)
