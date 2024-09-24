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
import java.time.LocalDateTime

@Entity
@Table(name = "group_meetings")
data class GroupMeetingEntity(
    @Column(name = "groupId", nullable = false)
    val groupId: Long,

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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun toSchedule(): Schedule {
        return Schedule(
            start = start,
            end = end
        )
    }
}
