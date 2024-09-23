package org.example.freetime.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.Schedules
import java.time.LocalDate

@Schema(description = "일별 스케줄 응답")
data class DailyScheduleResponse(
    @Schema(description = "날짜")
    val date: LocalDate,
    @Schema(description = "요일")
    val day: String,
    @Schema(description = "빈 시간(미팅 시간 제외됨)")
    val freeTime: List<ScheduleResponse>,
    @Schema(description = "미팅 시간 (빈 시간 보다 상위 노출)")
    val confirmedMeetings: List<ScheduleResponse>
){
    companion object{
        fun from(date: LocalDate, schedules: Schedules): DailyScheduleResponse {
            return DailyScheduleResponse(
                date = date,
                day = date.dayOfWeek.toString(),
                freeTime = ScheduleResponse.from(schedules.freeTime),
                confirmedMeetings = ScheduleResponse.from(schedules.confirmedMeetings)
            )
        }
    }
}
