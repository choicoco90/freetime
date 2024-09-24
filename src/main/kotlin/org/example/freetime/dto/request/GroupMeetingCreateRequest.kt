package org.example.freetime.dto.request

import java.time.LocalDateTime

data class GroupMeetingCreateRequest(
    val description: String,
    val place: String,
    val start: LocalDateTime,
    val end: LocalDateTime,
)
