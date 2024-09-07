package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.Schedules
import org.example.freetime.entities.UserEntity
import java.time.LocalDate

@Schema(description = "빈 시간 게스트 응답 (ReadOnly, 미팅 정보 미포함)")
data class FreeTimeGuestResponse(
    @Schema(description = "사용자 정보")
    val user: UserResponse,
    @Schema(description = "날짜별 스케줄")
    val schedules: List<DailyScheduleResponse>
){
    companion object {
        fun from(user: UserEntity, dateMap: Map<LocalDate, Schedules>): FreeTimeGuestResponse {
            return FreeTimeGuestResponse(
                user = UserResponse.from(user),
                schedules = dateMap.map { DailyScheduleResponse.from(it.key, it.value) }
            )
        }
    }
}
