package org.example.freetime.model


data class FreeTimes(
    val times: List<FreeTime>
){
    companion object {
        fun from(times: List<FreeTime>): FreeTimes {
            return FreeTimes(times).also { it.validate() }
        }
    }
    fun validate() {
        val hasOverlap = hasOverlappingFreeTimeIntervals(times)
        if (hasOverlap) {
            throw IllegalArgumentException("겹치는 시간대가 존재합니다.")
        }
    }
    private fun hasOverlappingFreeTimeIntervals(freeTimes: List<FreeTime>): Boolean {
        return freeTimes.indices.any { i ->
            freeTimes.drop(i + 1).any { otherFreeTime ->
                val currentStart = toMinutes(freeTimes[i].startHour, freeTimes[i].startMinute)
                val currentEnd = toMinutes(freeTimes[i].endHour, freeTimes[i].endMinute)
                val otherStart = toMinutes(otherFreeTime.startHour, otherFreeTime.startMinute)
                val otherEnd = toMinutes(otherFreeTime.endHour, otherFreeTime.endMinute)

                currentStart < otherEnd && otherStart < currentEnd  // Check if the intervals overlap
            }
        }
    }
    private fun toMinutes(hour: Int, minute: Int): Int {
        return hour * 60 + minute
    }
}
