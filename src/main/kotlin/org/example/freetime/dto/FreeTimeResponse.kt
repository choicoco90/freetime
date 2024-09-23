package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.FreeTime

@Schema(description = "빈 시간 응답")
data class FreeTimeResponse(
    @Schema(description = "빈 시간 시작 시간 (시)", example = "6", nullable = false)
    val startHour: Int,
    @Schema(description = "빈 시간 시작 시간 (분)", example = "30", nullable = false)
    val startMinute: Int,
    @Schema(description = "빈 시간 종료 시간 (시)", example = "9", nullable = false)
    val endHour: Int,
    @Schema(description = "빈 시간 종료 시간 (분)", example = "0", nullable = false)
    val endMinute: Int
){
    companion object{
        fun of(freeTime: FreeTime): FreeTimeResponse {
            return FreeTimeResponse(
                freeTime.startHour,
                freeTime.startMinute,
                freeTime.endHour,
                freeTime.endMinute
            )
        }
    }
}
