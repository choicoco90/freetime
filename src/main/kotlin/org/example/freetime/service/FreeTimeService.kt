package org.example.freetime.service

import org.example.freetime.domain.FreeTimeDailyUpdateCommand
import org.example.freetime.domain.FreeTimeWeeklyUpdateCommand
import org.example.freetime.domain.Schedules
import org.example.freetime.entities.DailyFreeTimeEntity
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.entities.WeeklyFreeTimeEntity
import org.example.freetime.enum.MeetingStatus
import org.example.freetime.repository.DailyFreeTimeRepository
import org.example.freetime.repository.MeetingRepository
import org.example.freetime.repository.WeeklyFreeTimeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class FreeTimeService(
    val weeklyFreeTimeRepository: WeeklyFreeTimeRepository,
    val dailyFreeTimeRepository: DailyFreeTimeRepository,
    val meetingRepository: MeetingRepository
) {
    @Transactional(readOnly = false)
    fun getSchedules(userId: Long, start: LocalDate, end: LocalDate): Map<LocalDate, Schedules> {
        val weeklyFreeTime = weeklyFreeTimeRepository.findByUserId(userId) ?: throw RuntimeException("Weekly free times not found")
        val dailyFreeTimes = dailyFreeTimeRepository.findAllByUserIdAndDateBetween(userId, start, end)
        val meetings = meetingRepository.findAllByUserIdAndStartBetweenAndStatus(userId, start.atStartOfDay(), end.atStartOfDay(), MeetingStatus.ACCEPTED)
        val dates = start.datesUntil(end).toList()
        return dates.associateWith { getScheduleOfDate(it, weeklyFreeTime, dailyFreeTimes, meetings) }
    }

    private fun getScheduleOfDate(
        date: LocalDate,
        weeklyFreeTime: WeeklyFreeTimeEntity,
        dailyFreeTimes: List<DailyFreeTimeEntity>,
        meetingEntities: List<MeetingEntity>
    ): Schedules {
        val weekly = weeklyFreeTime.getFreeTime(date.dayOfWeek).map { it.toSchedule(date) }
        val daily = dailyFreeTimes.find { it.date == date }?.freeTime?.map { it.toSchedule(date) }
        val meetings = meetingEntities.filter { it.start.toLocalDate() == date }.map { it.toSchedule() }
        return Schedules.of(freeTime = daily ?: weekly, meetings = meetings)
    }
    @Transactional(readOnly = false)
    fun createWeeklyFreeTimes(userId: Long, command: FreeTimeWeeklyUpdateCommand) {
        val weeklyFreeTime = command.toEntity(userId)
        weeklyFreeTimeRepository.save(weeklyFreeTime)
    }
    @Transactional(readOnly = false)
    fun updateWeeklyFreeTimes(userId: Long, command: FreeTimeWeeklyUpdateCommand) {
        val weeklyFreeTime = weeklyFreeTimeRepository.findByUserId(userId) ?: throw RuntimeException("Weekly free times not found")
        weeklyFreeTime.update(command)
        weeklyFreeTimeRepository.save(weeklyFreeTime)
    }
    @Transactional(readOnly = false)
    fun updateDailyFreeTimes(userId: Long, command: FreeTimeDailyUpdateCommand) {
        val saved = dailyFreeTimeRepository.findByUserIdAndDate(userId, command.date)
        if(saved == null) {
            dailyFreeTimeRepository.save(command.toEntity(userId))
        } else {
            saved.update(command.freeTime.times)
            dailyFreeTimeRepository.save(saved)
        }
    }
}
