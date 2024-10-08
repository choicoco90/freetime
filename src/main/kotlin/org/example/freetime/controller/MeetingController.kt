package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.request.GroupMeetingCreateRequest
import org.example.freetime.dto.response.MeetingResponse
import org.example.freetime.dto.request.MeetingUpdateRequest
import org.example.freetime.service.GroupMeetingService
import org.example.freetime.service.MeetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/meetings")
@Tag(name = "미팅 관리 API")
class MeetingController(
    val meetingService: MeetingService,
) {

    @Operation(summary = "미팅 상태 수정(취소)")
    @PutMapping("/{meetingId}/cancel")
    fun cancelMeeting(
        @RequestAttribute("userId") userId: Long,
        @PathVariable meetingId: Long
    ) {
        meetingService.cancelMeeting(meetingId, userId)
    }

    @Operation(summary = "미팅 상태 수정(승인)")
    @PutMapping("/{meetingId}/accept")
    fun acceptMeeting(
        @RequestAttribute("userId") userId: Long,
        @PathVariable meetingId: Long
    ) {
        meetingService.acceptMeeting(meetingId, userId)
    }

    @Operation(summary = "미팅 정보 수정")
    @PutMapping("/{meetingId}")
    fun updateMeetingInfo(
        @RequestAttribute("userId") userId: Long,
        @PathVariable meetingId: Long,
        @RequestBody request: MeetingUpdateRequest
    ) {
        meetingService.updateMeeting(meetingId, request, userId)
    }



}
