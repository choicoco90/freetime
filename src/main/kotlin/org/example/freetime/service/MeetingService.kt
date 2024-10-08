package org.example.freetime.service

import org.example.freetime.dto.request.MeetingUpdateRequest
import org.example.freetime.dto.request.ProposalAcceptRequest
import org.example.freetime.dto.request.ProposalCreateRequest
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
import java.time.LocalDateTime

// TODO: 여기에 이메일 보내는 기능을 추가
@Service
class MeetingService(
    val proposalRepository: ProposalRepository,
    val userRepository: UserRepository,
    val meetingRepository: MeetingRepository
) {
    @Transactional(readOnly = false)
    fun registerProposal(requesterId: Long, request: ProposalCreateRequest) {
        val requester = userRepository.findById(requesterId).orElseThrow { throw BizException(ErrorCode.USER_NOT_FOUND) }
        val proposal = ProposalEntity(
            userId = request.targetId,
            requesterId = requester.id,
            requesterName = requester.name,
            schedules = request.schedules.map { s -> s.toDomain() },
            expiredAt = request.expiredAt,
            place = request.place,
            description = request.description
        )
        proposalRepository.save(proposal)
    }
    @Transactional(readOnly = false)
    fun acceptProposal(userId: Long, proposalId: Long, request: ProposalAcceptRequest) {
        val schedule = request.schedule.toDomain()
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
            description = request.description,
            place = request.place
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
        val hasChange = meeting.hasChanged(request.start, request.end, request.description, request.place)
        if (hasChange){
            meeting.update(request.start, request.end, request.description, request.place)
            meetingRepository.save(meeting)
        }
    }


}
