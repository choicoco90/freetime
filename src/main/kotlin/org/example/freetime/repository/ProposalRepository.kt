package org.example.freetime.repository

import org.example.freetime.entities.ProposalEntity
import org.example.freetime.enum.ProposalStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ProposalRepository: JpaRepository<ProposalEntity, Long> {
    fun findAllByUserId(userId: Long): List<ProposalEntity>

    fun findAllByUserIdAndExpiredAtGreaterThanEqualAndStatus(userId: Long, expiredAt: LocalDateTime, status: ProposalStatus): List<ProposalEntity>
}
