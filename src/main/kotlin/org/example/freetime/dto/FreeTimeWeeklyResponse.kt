package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.FreeTimeWeeklyUpdateCommand
import org.example.freetime.domain.FreeTimes
import org.example.freetime.entities.WeeklyFreeTimeEntity

@Schema(description = "주간 빈 시간 응답")
data class FreeTimeWeeklyResponse(
    @Schema(description = "월요일 빈 시간")
    val monday: List<FreeTimeResponse>,
    @Schema(description = "화요일 빈 시간")
    val tuesday: List<FreeTimeResponse>,
    @Schema(description = "수요일 빈 시간")
    val wednesday: List<FreeTimeResponse>,
    @Schema(description = "목요일 빈 시간")
    val thursday: List<FreeTimeResponse>,
    @Schema(description = "금요일 빈 시간")
    val friday: List<FreeTimeResponse>,
    @Schema(description = "토요일 빈 시간")
    val saturday: List<FreeTimeResponse>,
    @Schema(description = "일요일 빈 시간")
    val sunday: List<FreeTimeResponse>
){
    companion object{
        fun of(entity: WeeklyFreeTimeEntity): FreeTimeWeeklyResponse{
            return FreeTimeWeeklyResponse(
                entity.monday.map { FreeTimeResponse.of(it) },
                entity.tuesday.map { FreeTimeResponse.of(it) },
                entity.wednesday.map { FreeTimeResponse.of(it) },
                entity.thursday.map { FreeTimeResponse.of(it) },
                entity.friday.map { FreeTimeResponse.of(it) },
                entity.saturday.map { FreeTimeResponse.of(it) },
                entity.sunday.map { FreeTimeResponse.of(it) }
            )
        }
    }

}
