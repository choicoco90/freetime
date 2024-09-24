package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.model.Schedule
import org.example.freetime.enums.MeetingStatus
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import java.time.LocalDateTime

@Entity
@Table(name = "meetings")
data class MeetingEntity(
    /**
     * 미팅의 주인 (개인 미팅의 경우 미팅 요청을 받은 사람, 그룹 미팅의 경우 그룹의 주인)
     */
    @Column(name= "userId", nullable = false)
    val userId: Long,

    @Column(name = "requesterId", nullable = false)
    val requesterId: Long,

    @Column(name = "requesterName", nullable = false)
    val requesterName: String,

    @Column(name = "start", nullable = false)
    var start: LocalDateTime,

    @Column(name = "end", nullable = false)
    var end: LocalDateTime,

    @Column(name = "place", nullable = false)
    var place: String,

    @Column(name = "description", nullable = false)
    var description: String = "",

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MeetingStatus = MeetingStatus.ACCEPTED
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun updateStatus( status: MeetingStatus){
        this.status = status
    }

    fun update(start: LocalDateTime, end: LocalDateTime, description: String, place: String) {
        this.start = start
        this.end = end
        this.description = description
        this.place = place
    }

    fun hasChanged(start: LocalDateTime, end: LocalDateTime, description: String, place: String): Boolean {
        return this.start != start || this.end != end || this.description != description || this.place != place
    }

    fun toSchedule(): Schedule {
        return Schedule(
            start = start,
            end = end
        )
    }

    fun validateUpdate(userId: Long) {
        val updatePossibleIds = listOf(requesterId, userId)
        if(userId !in updatePossibleIds) throw BizException(ErrorCode.MEETING_IS_NOT_MINE)
    }

}
