package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.converter.SchedulesConverter
import org.example.freetime.model.Schedule
import org.example.freetime.enums.ProposalStatus
import java.time.LocalDateTime

@Entity
@Table(name = "proposal")
data class ProposalEntity(
    /**
     * 요청을 받은 사람의 아이디(선택 권자)
     */
    @Column(name= "userId", nullable = false)
    val userId: Long,
    /**
     * 요청을 보낸 사람의 아이디
     */
    @Column(name= "requesterId", nullable = false)
    val requesterId: Long,

    @Column(name= "requesterName", nullable = false)
    val requesterName: String,

    @Column(name= "schedules", nullable = false, columnDefinition = "json")
    @Convert(converter = SchedulesConverter::class)
    val schedules: List<Schedule>,

    @Column(name= "expiredAt", nullable = false)
    val expiredAt: LocalDateTime,

    @Column(name= "place", nullable = false)
    val place: String,

    @Column(name= "description", nullable = false)
    val description: String,

    @Column(name= "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ProposalStatus = ProposalStatus.WAITING
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun accept(){
        status = ProposalStatus.ACCEPTED
    }

    fun reject(){
        status = ProposalStatus.REJECTED
    }
}
