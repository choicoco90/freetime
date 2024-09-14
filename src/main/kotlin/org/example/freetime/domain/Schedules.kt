package org.example.freetime.domain

import org.example.freetime.utils.FreeTimeUtils

data class Schedules(
    val freeTime: List<Schedule>,
    val confirmedMeetings: List<Schedule>
){
    companion object{
        fun of(freeTime: List<Schedule>, confirmedMeetings: List<Schedule>): Schedules {
            val excludedFreeTimes = FreeTimeUtils.excludeOverlap(freeTime, confirmedMeetings)
            return Schedules(excludedFreeTimes, confirmedMeetings)
        }
    }
}
