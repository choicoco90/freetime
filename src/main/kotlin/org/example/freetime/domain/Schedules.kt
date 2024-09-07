package org.example.freetime.domain

data class Schedules(
    val freeTime: List<Schedule>,
    val meetings: List<Schedule>
){
    companion object{
        fun of(freeTime: List<Schedule>, meetings: List<Schedule>): Schedules {
            return Schedules(freeTime, meetings)
        }
    }
}
