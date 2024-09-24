package org.example.freetime.domain

import org.example.freetime.entities.GroupEntity
import org.example.freetime.entities.GroupMeetingEntity

data class GroupMeeting(
    val groupMeeting: GroupMeetingEntity,
    val group: GroupEntity,
)
