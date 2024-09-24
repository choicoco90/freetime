package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.FreeTimeDailyUpdateCommand
import org.example.freetime.model.FreeTimes
import java.time.LocalDate

@Schema(description = "일별 빈 시간 업데이트 요청")
data class FreeTimeDailyRequest(
    @Schema(description = "날짜")
    val date: LocalDate,
    @Schema(description = "빈 시간")
    val freeTime: List<FreeTimeRequest>
) {
    fun toCommand(): FreeTimeDailyUpdateCommand {
        return FreeTimeDailyUpdateCommand(
            date,
            FreeTimes.from(freeTime.map { it.toDomain() })
        )
    }
}
