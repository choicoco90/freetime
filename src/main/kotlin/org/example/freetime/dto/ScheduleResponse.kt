package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.Schedule
import java.time.LocalDateTime

@Schema(description = "스케줄 응답")
data class ScheduleResponse(
    @Schema(description = "스케줄 시작 시간")
    val start: LocalDateTime,
    @Schema(description = "스케줄 종료 시간")
    val end: LocalDateTime
){
    companion object{
        fun from(typedSchedules: List<Schedule>): List<ScheduleResponse> {
            return typedSchedules.map {
                ScheduleResponse(
                    start = it.start,
                    end = it.end
                )
            }
        }
    }
}
