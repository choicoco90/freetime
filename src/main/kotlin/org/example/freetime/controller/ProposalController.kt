package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.ProposalAcceptRequest
import org.example.freetime.dto.ProposalCreateRequest
import org.example.freetime.dto.ProposalResponse
import org.example.freetime.service.MeetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/proposal")
@Tag(name = "미팅 제안 API")
class ProposalController(
    val meetingService: MeetingService
) {
    @GetMapping
    @Operation(description = "나의 모든 제안 조회")
    fun findAllProposal(
        @RequestAttribute("userId") userId: Long
    ): List<ProposalResponse> {
        return meetingService.findAllProposalsOfUser(userId).map {
            ProposalResponse.from(it)
        }
    }

    @GetMapping("/waiting")
    @Operation(description = "나의 대기 중이며, 유효한 제안 조회")
    fun findAllWaitingProposal(
        @RequestAttribute("userId") userId: Long
    ): List<ProposalResponse> {
        return meetingService.findAllUnExpiredWaitingProposals(userId).map {
            ProposalResponse.from(it)
        }
    }

    @PostMapping
    fun createProposal(
        @RequestBody request: List<ProposalCreateRequest>,
        @RequestAttribute("userId") userId: Long
    ) {
        meetingService.registerProposal(userId, request)
    }

    @PutMapping("/{proposalId}/accept")
    fun acceptProposal(
        @RequestAttribute("userId") userId: Long,
        @PathVariable proposalId: Long,
        @RequestBody request: ProposalAcceptRequest
    ) {
        meetingService.acceptProposal(userId, proposalId, request.schedule, request.description)
    }

    @PutMapping("/{proposalId}/reject")
    fun rejectProposal(
        @PathVariable proposalId: Long
    ) {
        meetingService.rejectProposal(proposalId)
    }

}
