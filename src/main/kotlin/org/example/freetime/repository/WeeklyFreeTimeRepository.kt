package org.example.freetime.repository

import org.example.freetime.entities.WeeklyFreeTimeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WeeklyFreeTimeRepository: JpaRepository<WeeklyFreeTimeEntity, Long> {
    fun findByUserId(userId: Long): WeeklyFreeTimeEntity?
}
