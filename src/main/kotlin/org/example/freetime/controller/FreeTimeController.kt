package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.response.FreeTimeGuestResponse
import org.example.freetime.dto.response.FreeTimeMyResponse
import org.example.freetime.dto.request.FreeTimeDailyRequest
import org.example.freetime.dto.request.FreeTimeWeeklyRequest
import org.example.freetime.dto.response.FreeTimeWeeklyResponse
import org.example.freetime.service.FreeTimeService
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
    val userService: UserService
) {
    @Operation(summary = "나의 빈 시간 조회 (메인 API)")
    @GetMapping
    fun getMyFreeTime(
        @RequestAttribute userId: Long,
        @RequestParam start: LocalDate,
        @RequestParam end: LocalDate
    ): FreeTimeMyResponse {
        val user = userService.getUserById(userId)
        val weeklySchedule = freeTimeService.getSchedules(userId, start, end.plusDays(1))
        return FreeTimeMyResponse.from(user, weeklySchedule)
    }

    @GetMapping("/{targetId}")
    @Operation(summary = "게스트 빈 시간 조회 (ReadOnly, 미팅 정보 미포함)")
    fun getFreeTimeReadOnly(
        @PathVariable targetId: Long,
        @RequestParam start: LocalDate,
        @RequestParam end: LocalDate
    ): FreeTimeGuestResponse {
        val target = userService.getUserById(targetId)
        val schedules = freeTimeService.getSchedules(targetId, start, end.plusDays(1))
        return FreeTimeGuestResponse.from(target, schedules)
    }


    @Operation(summary = "나의 설정된 주간 빈시간 조회")
    @GetMapping("/weekly")
    fun getMyWeeklyFreeTime(
        @RequestAttribute userId: Long,
    ): FreeTimeWeeklyResponse {
        val weeklyFreeTimeEntity = freeTimeService.getWeeklyFreeTimes(userId)
        return FreeTimeWeeklyResponse.of(weeklyFreeTimeEntity)
    }

    @PutMapping("/weekly")
    @Operation(summary = "주간 빈 시간 수정")
    fun updateWeeklyFreeTime(
        @RequestAttribute userId: Long,
        @RequestBody request: FreeTimeWeeklyRequest
    ) {
        val command = request.toCommand()
        freeTimeService.updateWeeklyFreeTimes(userId, command)
    }

    @PutMapping("/daily")
    @Operation(summary = "일간 빈 시간 수정 및 추가")
    fun updateDailyFreeTime(
        @RequestAttribute userId: Long,
        @RequestBody request: FreeTimeDailyRequest
    ) {
        val command = request.toCommand()
        freeTimeService.updateDailyFreeTimes(userId, command)
    }


    @GetMapping("/groups/{groupId}")
    @Operation(summary = "그룹 빈 시간 조회")
    fun getGroupFreeTime(
        @RequestAttribute userId: Long,
        @PathVariable groupId: Long,
        @RequestParam start: LocalDate,
        @RequestParam end: LocalDate
    ) {


    }

}
