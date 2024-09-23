package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.request.ProposalAcceptRequest
import org.example.freetime.dto.request.ProposalCreateRequest
import org.example.freetime.dto.response.ProposalResponse
import org.example.freetime.service.MeetingService
import org.example.freetime.utils.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/proposals")
@Tag(name = "미팅 제안 API")
class ProposalController(
    val meetingService: MeetingService
) {
    @GetMapping("/sent")
    @Operation(summary = "내가 보낸 미팅 제안 조회")
    fun findAllProposalSent(
        @RequestAttribute("userId") userId: Long
    ): List<ProposalResponse> {
        return meetingService.findAllProposalsSent(userId).map {
            ProposalResponse.from(it)
        }
    }

    @GetMapping("/waiting")
    @Operation(summary = "나의 대기 중이며, 유효한 미팅 제안 조회")
    fun findAllProposalReceiveWaiting(
        @RequestAttribute("userId") userId: Long
    ): List<ProposalResponse> {
        return meetingService.findAllUnExpiredWaitingProposals(userId).map {
            ProposalResponse.from(it)
        }
    }

    @Operation(summary = "미팅 제안 생성")
    @PostMapping
    fun createProposal(
        @RequestBody request: ProposalCreateRequest,
        @RequestAttribute("userId") userId: Long
    ) {
        logger().info("createProposal: $request")
        meetingService.registerProposal(userId, request)
    }

    @Operation(summary = "미팅 제안 수락")
    @PutMapping("/{proposalId}/accept")
    fun acceptProposal(
        @RequestAttribute("userId") userId: Long,
        @PathVariable proposalId: Long,
        @RequestBody request: ProposalAcceptRequest
    ) {
        meetingService.acceptProposal(userId, proposalId, request)
    }

    @Operation(summary = "미팅 제안 거절")
    @PutMapping("/{proposalId}/reject")
    fun rejectProposal(
        @RequestAttribute("userId") userId: Long,
        @PathVariable proposalId: Long
    ) {
        meetingService.rejectProposal(userId, proposalId)
    }

}
