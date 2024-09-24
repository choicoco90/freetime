package org.example.freetime.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.WeeklySchedule
import org.example.freetime.entities.UserEntity

@Schema(description = "나의 빈 시간 조회 응답")
data class FreeTimeMyResponse(
    @Schema(description = "사용자 정보")
    val user: UserResponse,
    @Schema(description = "일별 빈 시간 (날짜 순 정렬")
    val schedules: List<DailyScheduleResponse>,
    @Schema(description = "내가 받은 미팅 (승인, 취소 모두 포함)")
    val ownedMeetings: List<MeetingResponse>,
    @Schema(description = "내가 보낸 미팅 (승인, 취소 모두 포함)")
    val guestMeetings: List<MeetingResponse>,
    @Schema(description = "그룹 미팅")
    val groupMeetings: List<GroupMeetingResponse>
){
    companion object {
        fun from(user: UserEntity, weeklySchedule: WeeklySchedule): FreeTimeMyResponse {
            return FreeTimeMyResponse(
                user = UserResponse.from(user),
                schedules = weeklySchedule.schedules.map { (date, schedules) ->
                    DailyScheduleResponse.from(date, schedules)
                },
                ownedMeetings =  MeetingResponse.from(weeklySchedule.ownedMeetings),
                guestMeetings = MeetingResponse.from(weeklySchedule.guestMeetings),
                groupMeetings = GroupMeetingResponse.from(weeklySchedule.groupMeetings)
            )
        }
    }
}
