package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.Schedule
import java.time.LocalDateTime

@Schema(description = "제안 수락")
data class ProposalAcceptRequest(
    @Schema(description = "수락한 일정")
    val schedule: ScheduleRequest,
    @Schema(description = "미팅 설명 추가 (default 빈 문자열)")
    val description: String
)
