package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "미팅 수정 요청")
data class MeetingUpdateRequest(
    @Schema(description = "미팅 정보")
    val description: String,
    @Schema(description = "미팅 장소")
    val place: String,
    @Schema(description = "미팅 시작 시간")
    val start: LocalDateTime,
    @Schema(description = "미팅 종료 시간")
    val end: LocalDateTime,
)
