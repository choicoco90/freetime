package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.domain.Schedule
import org.example.freetime.enum.MeetingStatus
import java.time.LocalDateTime

@Entity
@Table(name = "meetings")
data class MeetingEntity(
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

    @Column(name = "description", nullable = false)
    var description: String = "",

    @Column(name = "status", nullable = false)
    var status: MeetingStatus = MeetingStatus.ACCEPTED
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun updateStatus( status: MeetingStatus){
        this.status = status
    }

    fun update(start: LocalDateTime, end: LocalDateTime, description: String){
        this.start = start
        this.end = end
        this.description = description
    }

    fun hasChanged(start: LocalDateTime, end: LocalDateTime, description: String): Boolean {
        return this.start != start || this.end != end || this.description != description
    }

    fun toSchedule(): Schedule {
        return Schedule(
            start = start,
            end = end
        )
    }
}
