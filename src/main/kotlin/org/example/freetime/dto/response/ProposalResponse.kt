package org.example.freetime.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.entities.ProposalEntity
import org.example.freetime.enums.ProposalStatus
import java.time.LocalDateTime

@Schema(description = "미팅 제안 응답")
data class ProposalResponse(
    @Schema(description = "미팅 제안 ID")
    val proposalId: Long,
    @Schema(description = "제안자 ID")
    val requesterId: Long,
    @Schema(description = "대상 ID")
    val targetId: Long,
    @Schema(description = "제안자 이름")
    val requesterName: String,
    @Schema(description = "미팅 일정 목록")
    val schedules: List<ScheduleResponse>,
    @Schema(description = "만료 시간")
    val expiredAt: LocalDateTime,
    @Schema(description = "장소")
    val place: String,
    @Schema(description = "제안 설명")
    val description: String,
    @Schema(description = "제안 상태 (대기중, 수락, 거절)")
    val status: ProposalStatus
    ){
    companion object{
        fun from(entity: ProposalEntity): ProposalResponse {
            return ProposalResponse(
                proposalId = entity.id,
                requesterId = entity.requesterId,
                targetId = entity.userId,
                requesterName = entity.requesterName,
                schedules = ScheduleResponse.from(entity.schedules),
                expiredAt = entity.expiredAt,
                place = entity.place,
                description = entity.description,
                status = entity.status
            )
        }
    }
}
