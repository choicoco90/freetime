package org.example.freetime.service

import org.example.freetime.domain.Schedule
import org.example.freetime.dto.MeetingUpdateRequest
import org.example.freetime.dto.ProposalCreateRequest
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.entities.ProposalEntity
import org.example.freetime.enum.MeetingStatus
import org.example.freetime.enum.ProposalStatus
import org.example.freetime.repository.MeetingRepository
import org.example.freetime.repository.ProposalRepository
import org.example.freetime.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

// TODO: 여기에 이메일 보내는 기능을 추가
@Service
class MeetingService(
    val proposalRepository: ProposalRepository,
    val userRepository: UserRepository,
    val meetingRepository: MeetingRepository
) {
    @Transactional(readOnly = false)
    fun registerProposal(requesterId: Long, request: List<ProposalCreateRequest>) {
        val requester = userRepository.findById(requesterId).orElseThrow { throw RuntimeException("User not found") }
        request.map {
            ProposalEntity(
                userId = it.targetId,
                requesterId = requester.id,
                requesterName = requester.name,
                schedules = it.schedules,
                expiredAt = it.expiredAt
            )
        }.also { proposalRepository.saveAll(it) }
    }
    @Transactional(readOnly = false)
    fun acceptProposal(userId: Long, proposalId: Long, schedule: Schedule, description: String) {
        val proposal = proposalRepository.findById(proposalId).orElseThrow { throw RuntimeException("Proposal not found") }
        val foundSchedule = proposal.schedules.find { it == schedule } ?: throw RuntimeException("Schedule not found")
        val meeting = MeetingEntity(
            userId = userId,
            requesterId = proposal.requesterId,
            requesterName = proposal.requesterName,
            start = foundSchedule.start,
            end = foundSchedule.end,
            description = description
        )
        proposal.accept()
        proposalRepository.save(proposal)
        meetingRepository.save(meeting)
    }
    @Transactional(readOnly = false)
    fun rejectProposal(proposalId: Long) {
        val proposal = proposalRepository.findById(proposalId).orElseThrow { throw RuntimeException("Proposal not found") }
        proposal.reject()
        proposalRepository.save(proposal)
    }
    @Transactional(readOnly = true)
    fun findAllUnExpiredWaitingProposals(userId: Long): List<ProposalEntity> {
        val now = LocalDateTime.now()
        return proposalRepository.findAllByUserIdAndExpiredAtGreaterThanEqualAndStatus(userId, now, ProposalStatus.WAITING)
    }
    @Transactional(readOnly = true)
    fun findAllProposalsOfUser(userId: Long): List<ProposalEntity> {
        return proposalRepository.findAllByUserId(userId)
    }

    // ----- Meeting ----- //
    @Transactional(readOnly = true)
    fun findAllMeetingsOfPeriod(userId: Long, from: LocalDate, to: LocalDate): List<MeetingEntity> {
        return meetingRepository.findAllByUserIdAndStartBetween(userId, from.atStartOfDay(), to.atStartOfDay())
    }
    @Transactional(readOnly = false)
    fun updateMeetingStatus(meetingId: Long, status: MeetingStatus) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow { throw RuntimeException("Meeting not found") }
        meeting.updateStatus(status)
        meetingRepository.save(meeting)
    }
    @Transactional(readOnly = false)
    fun updateMeeting(meetingId: Long, request: MeetingUpdateRequest) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow { throw RuntimeException("Meeting not found") }

        val hasChange = meeting.hasChanged(request.start, request.end, request.description)
        if (hasChange){
            meeting.update(request.start, request.end, request.description)
            meetingRepository.save(meeting)
            // TODO: 이메일 보내는 기능 추가
        }
    }
}
