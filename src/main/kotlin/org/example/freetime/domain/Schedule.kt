package org.example.freetime.domain

import java.time.LocalDateTime

data class Schedule(
    val start: LocalDateTime,
    val end: LocalDateTime
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Schedule) return false
        return this.start == other.start && this.end == other.end
    }


    override fun hashCode(): Int {
        return super.hashCode()
    }
}
