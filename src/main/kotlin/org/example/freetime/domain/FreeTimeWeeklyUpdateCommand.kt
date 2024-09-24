package org.example.freetime.domain

import org.example.freetime.entities.WeeklyFreeTimeEntity
import org.example.freetime.model.FreeTimes

data class FreeTimeWeeklyUpdateCommand(
    val monday: FreeTimes,
    val tuesday: FreeTimes,
    val wednesday: FreeTimes,
    val thursday: FreeTimes,
    val friday: FreeTimes,
    val saturday: FreeTimes,
    val sunday: FreeTimes
){
    fun toEntity(userId: Long): WeeklyFreeTimeEntity {
        return WeeklyFreeTimeEntity(
            userId = userId,
            monday = monday.times,
            tuesday = tuesday.times,
            wednesday = wednesday.times,
            thursday = thursday.times,
            friday = friday.times,
            saturday = saturday.times,
            sunday = sunday.times
        )
    }
}
