package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "제안 수락")
data class ProposalAcceptRequest(
    @Schema(description = "수락한 일정")
    val schedule: ScheduleRequest,
    @Schema(description = "미팅 장소 (변경 없으면 proposal 의 장소로 설정)")
    val place: String,
    @Schema(description = "미팅 설명 추가 (변경 없으면 proposal 의 설명 그대로 유지, 있으면 추가)")
    val description: String
)
