package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.FreeTimeGuestResponse
import org.example.freetime.dto.FreeTimeMyResponse
import org.example.freetime.dto.FreeTimeDailyRequest
import org.example.freetime.dto.FreeTimeWeeklyRequest
import org.example.freetime.service.FreeTimeService
import org.example.freetime.service.MeetingService
import org.example.freetime.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/free-time")
@Tag(name = "빈 시간 관련 API")
class FreeTimeController(
    val freeTimeService: FreeTimeService,
    val userService: UserService,
    val meetingService: MeetingService
) {
    @Operation(summary = "나의 빈 시간 조회 (메인 API)")
    @GetMapping
    fun getMyFreeTime(
        @RequestAttribute userId: Long,
        @RequestParam start: LocalDate,
        @RequestParam end: LocalDate
    ): FreeTimeMyResponse {
        val user = userService.getUserById(userId)
        val meetings = meetingService.findAllMeetingsOfPeriod(userId, start, end.plusDays(1))
        val schedules = freeTimeService.getSchedules(userId, start, end.plusDays(1))
        return FreeTimeMyResponse.from(user, schedules, meetings)
    }

    @GetMapping("/{userId}")
    @Operation(summary = "게스트 빈 시간 조회 (ReadOnly, 미팅 정보 미포함)")
    fun getFreeTimeReadOnly(
        @PathVariable userId: Long,
        @RequestParam start: LocalDate,
        @RequestParam end: LocalDate
    ): FreeTimeGuestResponse {
        val user = userService.getUserById(userId)
        val schedules = freeTimeService.getSchedules(userId, start, end)
        return FreeTimeGuestResponse.from(user, schedules)
    }

    @PutMapping("/update/weekly")
    @Operation(summary = "주간 빈 시간 수정")
    fun updateWeekly(
        @RequestAttribute userId: Long,
        @RequestBody request: FreeTimeWeeklyRequest
    ) {
        val command = request.toCommand()
        freeTimeService.updateWeeklyFreeTimes(userId, command)
    }

    @PutMapping("/update/daily")
    @Operation(summary = "일간 빈 시간 수정 및 추가")
    fun updateDaily(
        @RequestAttribute userId: Long,
        @RequestBody request: FreeTimeDailyRequest
    ) {
        val command = request.toCommand()
        freeTimeService.updateDailyFreeTimes(userId, command)
    }

}
