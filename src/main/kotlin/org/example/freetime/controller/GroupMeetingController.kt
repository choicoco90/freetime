package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.request.GroupMeetingCreateRequest
import org.example.freetime.dto.request.MeetingUpdateRequest
import org.example.freetime.service.GroupMeetingService
import org.example.freetime.service.MeetingService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/group-meetings")
@Tag(name = "그룹 미팅 관리 API")
class GroupMeetingController(
    val groupMeetingService: GroupMeetingService
) {


    @Operation(summary = "그룹 미팅 생성")
    @PostMapping("/{groupId}")
    fun createGroupMeeting(
        @RequestAttribute("userId") userId: Long,
        @PathVariable groupId: Long,
        @RequestBody request: GroupMeetingCreateRequest
    ) {
        groupMeetingService.createGroupMeeting(
            requesterId = userId,
            groupId = groupId,
            request = request
        )
    }

    @Operation(summary = "그룹 미팅 상태 수정(취소)")
    @PutMapping("/{meetingId}/cancel")
    fun cancelGroupMeeting(
        @RequestAttribute("userId") userId: Long,
        @PathVariable meetingId: Long
    ) {
        groupMeetingService.cancelGroupMeeting(meetingId, userId)
    }



}
