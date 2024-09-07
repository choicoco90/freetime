package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.converter.FreeTimeConverter
import org.example.freetime.domain.FreeTime
import org.example.freetime.domain.FreeTimeWeeklyUpdateCommand
import java.time.DayOfWeek

@Table(name = "weekly_free_times")
@Entity
data class WeeklyFreeTimeEntity(
    @Id
    @Column(name= "userId", nullable = false)
    val userId: Long,

    @Column(name = "monday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var monday: List<FreeTime>,

    @Column(name = "tuesday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var tuesday: List<FreeTime>,

    @Column(name = "wednesday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var wednesday: List<FreeTime>,

    @Column(name = "thursday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var thursday: List<FreeTime>,

    @Column(name = "friday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var friday: List<FreeTime>,

    @Column(name = "saturday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var saturday: List<FreeTime>,

    @Column(name = "sunday", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var sunday: List<FreeTime>
){
    fun getFreeTime(day: DayOfWeek): List<FreeTime> {
        return when(day){
            DayOfWeek.MONDAY -> monday
            DayOfWeek.TUESDAY -> tuesday
            DayOfWeek.WEDNESDAY -> wednesday
            DayOfWeek.THURSDAY -> thursday
            DayOfWeek.FRIDAY -> friday
            DayOfWeek.SATURDAY -> saturday
            DayOfWeek.SUNDAY -> sunday
            else -> throw RuntimeException("Invalid day")
        }
    }
    fun update(command: FreeTimeWeeklyUpdateCommand){
        monday = command.monday.times
        tuesday = command.tuesday.times
        wednesday = command.wednesday.times
        thursday = command.thursday.times
        friday = command.friday.times
        saturday = command.saturday.times
        sunday = command.sunday.times
    }
}
