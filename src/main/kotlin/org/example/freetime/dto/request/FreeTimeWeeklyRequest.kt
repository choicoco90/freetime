package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.FreeTimeWeeklyUpdateCommand
import org.example.freetime.domain.FreeTimes

@Schema(description = "주간 빈 시간 업데이트 요청")
data class FreeTimeWeeklyRequest(
    @Schema(description = "월요일 빈 시간")
    val monday: List<FreeTimeRequest>,
    @Schema(description = "화요일 빈 시간")
    val tuesday: List<FreeTimeRequest>,
    @Schema(description = "수요일 빈 시간")
    val wednesday: List<FreeTimeRequest>,
    @Schema(description = "목요일 빈 시간")
    val thursday: List<FreeTimeRequest>,
    @Schema(description = "금요일 빈 시간")
    val friday: List<FreeTimeRequest>,
    @Schema(description = "토요일 빈 시간")
    val saturday: List<FreeTimeRequest>,
    @Schema(description = "일요일 빈 시간")
    val sunday: List<FreeTimeRequest>
){
    fun toCommand(): FreeTimeWeeklyUpdateCommand {
        return FreeTimeWeeklyUpdateCommand(
            FreeTimes.from(monday.map { it.toDomain()}),
            FreeTimes.from(tuesday.map { it.toDomain() }),
            FreeTimes.from(wednesday.map { it.toDomain() }),
            FreeTimes.from(thursday.map { it.toDomain() }),
            FreeTimes.from(friday.map { it.toDomain() }),
            FreeTimes.from(saturday.map { it.toDomain() }),
            FreeTimes.from(sunday.map { it.toDomain() })
        )
    }
}
