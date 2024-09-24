package org.example.freetime.domain

import org.example.freetime.dto.response.GroupMeetingResponse
import org.example.freetime.dto.response.MeetingResponse
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.model.Schedules
import java.time.LocalDate

data class WeeklySchedule(
    val schedules: Map<LocalDate, Schedules>,
    val ownedMeetings: List<MeetingEntity>,
    val guestMeetings: List<MeetingEntity>,
    val groupMeetings: List<GroupMeeting>
)
