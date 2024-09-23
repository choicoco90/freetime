package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "제안 생성 요청")
data class ProposalCreateRequest(
    @Schema(description = "대상 ID")
    val targetId: Long,
    @Schema(description = "제안 장소 (default: 빈 문자열)")
    val place: String,
    @Schema(description = "제안 일정 (다수)")
    val schedules: List<ScheduleRequest>,
    @Schema(description = "제안 만료 시간 (default: 제안 일정 마지막 시간)")
    val expiredAt: LocalDateTime,
    @Schema(description = "제안 설명")
    val description: String
)
