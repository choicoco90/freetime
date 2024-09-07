package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.Schedules
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.entities.UserEntity
import java.time.LocalDate

@Schema(description = "나의 빈 시간 조회 응답")
data class FreeTimeMyResponse(
    @Schema(description = "사용자 정보")
    val user: UserResponse,
    @Schema(description = "일별 빈 시간 (날짜 순 정렬")
    val schedules: List<DailyScheduleResponse>,
    @Schema(description = "기간 미팅 목록 (승인, 취소)")
    val meetings: List<MeetingResponse>,
){
    companion object {
        fun from(user: UserEntity, dateMap: Map<LocalDate, Schedules>, meetings: List<MeetingEntity>): FreeTimeMyResponse {
            return FreeTimeMyResponse(
                user = UserResponse.from(user),
                schedules = dateMap.map { DailyScheduleResponse.from(it.key, it.value) },
                meetings =  MeetingResponse.from(meetings)
            )
        }
    }
}
