package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.converter.FreeTimeConverter
import org.example.freetime.domain.FreeTime
import java.time.LocalDate

@Entity
@Table(name = "daily_free_times")
data class DailyFreeTimeEntity(
    @Column(name= "userId", nullable = false)
    val userId: Long,

    @Column(name = "date", nullable = false)
    val date: LocalDate,

    @Column(name = "freeTime", nullable = false, columnDefinition = "json")
    @Convert(converter = FreeTimeConverter::class)
    var freeTime: List<FreeTime>
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun update(freeTime: List<FreeTime>){
        this.freeTime = freeTime
    }
}
