package org.example.freetime.service

import org.example.freetime.domain.Schedule
import org.example.freetime.dto.MeetingUpdateRequest
import org.example.freetime.dto.ProposalCreateRequest
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.entities.ProposalEntity
import org.example.freetime.enums.MeetingStatus
import org.example.freetime.enums.ProposalStatus
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
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
        val requester = userRepository.findById(requesterId).orElseThrow { throw BizException(ErrorCode.USER_NOT_FOUND) }
        request.map {
            ProposalEntity(
                userId = it.targetId,
                requesterId = requester.id,
                requesterName = requester.name,
                schedules = it.schedules.map { s -> s.toDomain() },
                expiredAt = it.expiredAt,
                description = it.description
            )
        }.also { proposalRepository.saveAll(it) }
    }
    @Transactional(readOnly = false)
    fun acceptProposal(userId: Long, proposalId: Long, schedule: Schedule, description: String) {
        val proposal = proposalRepository.findById(proposalId).orElseThrow { throw BizException(ErrorCode.PROPOSAL_NOT_FOUND) }
        if (proposal.userId != userId) throw BizException(ErrorCode.PROPOSAL_IS_NOT_MINE)
        if (proposal.status != ProposalStatus.WAITING) throw BizException(ErrorCode.PROPOSAL_NOT_WAITING)
        if (proposal.expiredAt.isBefore(LocalDateTime.now())) throw BizException(ErrorCode.PROPOSAL_EXPIRED)
        val foundSchedule = proposal.schedules.find { it == schedule } ?: throw BizException(ErrorCode.PROPOSAL_SCHEDULE_NOT_FOUND)
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
    fun rejectProposal(userId: Long, proposalId: Long) {
        val proposal = proposalRepository.findById(proposalId).orElseThrow { throw BizException(ErrorCode.PROPOSAL_NOT_FOUND) }
        if(proposal.userId != userId) throw BizException(ErrorCode.PROPOSAL_IS_NOT_MINE)
        if(proposal.status != ProposalStatus.WAITING) throw BizException(ErrorCode.PROPOSAL_NOT_WAITING)
        proposal.reject()
        proposalRepository.save(proposal)
    }
    @Transactional(readOnly = true)
    fun findAllUnExpiredWaitingProposals(userId: Long): List<ProposalEntity> {
        val now = LocalDateTime.now()
        return proposalRepository.findAllByUserIdAndExpiredAtGreaterThanEqualAndStatus(userId, now, ProposalStatus.WAITING)
    }
    @Transactional(readOnly = true)
    fun findAllProposalsSent(userId: Long): List<ProposalEntity> {
        return proposalRepository.findAllByRequesterId(userId)
    }

    // ----- Meeting ----- //
    @Transactional(readOnly = true)
    fun findAllMeetingsOfPeriod(userId: Long, from: LocalDate, to: LocalDate): List<MeetingEntity> {
        return meetingRepository.findAllByUserIdAndStartBetween(userId, from.atStartOfDay(), to.atStartOfDay())
    }
    @Transactional(readOnly = false)
    fun acceptMeeting(meetingId: Long, userId: Long) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow { throw BizException(ErrorCode.MEETING_NOT_FOUND) }
        meeting.validateUpdate(userId)
        if(meeting.userId != userId) throw BizException(ErrorCode.MEETING_IS_NOT_MINE)
        meeting.updateStatus(MeetingStatus.ACCEPTED)
        meetingRepository.save(meeting)
    }

    @Transactional(readOnly = false)
    fun cancelMeeting(meetingId: Long, userId: Long) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow { throw BizException(ErrorCode.MEETING_NOT_FOUND) }
        meeting.validateUpdate(userId)
        meeting.updateStatus(MeetingStatus.ACCEPTED)
        meetingRepository.save(meeting)
    }

    @Transactional(readOnly = false)
    fun updateMeeting(meetingId: Long, request: MeetingUpdateRequest, userId: Long) {
        val meeting = meetingRepository.findById(meetingId).orElseThrow { throw BizException(ErrorCode.MEETING_NOT_FOUND) }
        meeting.validateUpdate(userId)
        val hasChange = meeting.hasChanged(request.start, request.end, request.description)
        if (hasChange){
            meeting.update(request.start, request.end, request.description)
            meetingRepository.save(meeting)
        }
    }
}
