package org.example.freetime.service

import org.example.freetime.domain.FreeTimeDailyUpdateCommand
import org.example.freetime.domain.FreeTimeWeeklyUpdateCommand
import org.example.freetime.domain.Schedules
import org.example.freetime.dto.MeetingResponse
import org.example.freetime.dto.WeeklyScheduleInfoResponse
import org.example.freetime.entities.DailyFreeTimeEntity
import org.example.freetime.entities.MeetingEntity
import org.example.freetime.entities.WeeklyFreeTimeEntity
import org.example.freetime.enums.MeetingStatus
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
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
    fun getSchedules(userId: Long, start: LocalDate, end: LocalDate): WeeklyScheduleInfoResponse {
        val weeklyFreeTime = weeklyFreeTimeRepository.findByUserId(userId) ?: throw BizException(ErrorCode.WEEKLY_FREE_TIME_NOT_FOUND)
        val dailyFreeTimes = dailyFreeTimeRepository.findAllByUserIdAndDateBetween(userId, start, end)
        val ownedMeetings = meetingRepository.findAllByUserIdAndStartBetween(userId, start.atStartOfDay(), end.atStartOfDay())
        val guestMeetings = meetingRepository.findAllByRequesterIdAndStartBetween(userId, start.atStartOfDay(), end.atStartOfDay())
        val dates = start.datesUntil(end).toList()
        val schedules= dates.associateWith { getScheduleOfDate(it, weeklyFreeTime, dailyFreeTimes, ownedMeetings, guestMeetings) }
        return WeeklyScheduleInfoResponse(
            schedules = schedules,
            ownedMeetings = MeetingResponse.from(ownedMeetings),
            guestMeetings = MeetingResponse.from(guestMeetings)
        )
    }

    private fun getScheduleOfDate(
        date: LocalDate,
        weeklyFreeTime: WeeklyFreeTimeEntity,
        dailyFreeTimes: List<DailyFreeTimeEntity>,
        ownedMeetings: List<MeetingEntity>,
        guestMeetings: List<MeetingEntity>
    ): Schedules {
        val weekly = weeklyFreeTime.getFreeTime(date.dayOfWeek).map { it.toSchedule(date) }
        val daily = dailyFreeTimes.find { it.date == date }?.freeTime?.map { it.toSchedule(date) }
        val meetingEntities = ownedMeetings + guestMeetings
        val acceptedMeetings = meetingEntities.filter { it.start.toLocalDate() == date && it.status == MeetingStatus.ACCEPTED }.map { it.toSchedule() }
        return Schedules.of(freeTime = daily ?: weekly, confirmedMeetings = acceptedMeetings)
    }

    @Transactional(readOnly = false)
    fun updateWeeklyFreeTimes(userId: Long, command: FreeTimeWeeklyUpdateCommand) {
        val weeklyFreeTime = weeklyFreeTimeRepository.findByUserId(userId) ?: throw BizException(ErrorCode.WEEKLY_FREE_TIME_NOT_FOUND)
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
