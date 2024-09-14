package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.enums.MeetingStatus
import java.time.LocalDateTime

@Schema(description = "미팅 응답")
data class MeetingResponse(
    @Schema(description = "미팅 ID")
    val meetingId: Long,
    @Schema(description = "요청 수신자 ID")
    val receiverId: Long,
    @Schema(description = "요청자 ID")
    val requesterId: Long,
    @Schema(description = "요청자 이름")
    val requesterName: String,
    @Schema(description = "미팅 시작 시간")
    val start: LocalDateTime,
    @Schema(description = "미팅 종료 시간")
    val end: LocalDateTime,
    @Schema(description = "미팅 설명")
    val description: String,
    @Schema(description = "미팅 상태 (승인, 취소)")
    val status: MeetingStatus
){
    companion object{
        fun from(meetings: List<MeetingEntity>): List<MeetingResponse> {
            return meetings.map { MeetingResponse(it.id,it.userId, it.requesterId, it.requesterName, it.start, it.end, it.description, it.status) }
        }
    }
}
