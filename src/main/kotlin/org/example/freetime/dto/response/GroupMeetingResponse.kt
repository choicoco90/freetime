package org.example.freetime.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.domain.GroupMeeting
import org.example.freetime.enums.MeetingStatus
import java.time.LocalDateTime

@Schema(description = "그룹 미팅 응답")
data class GroupMeetingResponse(
    @Schema(description = "그룹 미팅 ID")
    val meetingId: Long,
    @Schema(description = "그룹 ID")
    val groupId: Long,
    @Schema(description = "그룹 이름")
    val groupName: String,

    @Schema(description = "미팅 시작 시간")
    val start: LocalDateTime,
    @Schema(description = "미팅 종료 시간")
    val end: LocalDateTime,
    @Schema(description = "미팅 장소")
    val place: String,
    @Schema(description = "미팅 설명")
    val description: String,
    @Schema(description = "미팅 상태 (승인, 취소)")
    val status: MeetingStatus
){
    companion object{
        fun from(meetings: List<GroupMeeting>): List<GroupMeetingResponse> {
            return meetings.map {
                val (meeting, group) = it
                GroupMeetingResponse(
                    meeting.id,
                    group.id,
                    group.groupName,
                    meeting.start,
                    meeting.end,
                    meeting.place,
                    meeting.description,
                    meeting.status
                )
            }
        }
    }
}
