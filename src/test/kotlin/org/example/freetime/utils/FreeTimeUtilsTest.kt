package org.example.freetime.utils

import org.example.freetime.domain.Schedule
import org.example.freetime.utils.FreeTimeUtils.excludeOverlap
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class FreeTimeUtilsTest {
    // Helper function to format time ranges for better readability
    private fun createSchedule(startHour: Int, endHour: Int): Schedule {
        val start = LocalDateTime.of(2024, 9, 1, startHour, 0)
        val end = LocalDateTime.of(2024, 9, 1, endHour, 0)
        return Schedule(start, end)
    }

    @Test
    fun `겹침 없는 자유시간과 미팅`() {
        // 자유시간 10-12, 미팅시간 13-15
        val freeTimes = listOf(createSchedule(10, 12))
        val meetings = listOf(createSchedule(13, 15))

        val result = excludeOverlap(freeTimes, meetings)

        // 10-12 시간이 유지되어야 함
        assertEquals(listOf(createSchedule(10, 12)), result)
    }

    @Test
    fun `자유시간이 미팅에 완전히 덮임`() {
        // 자유시간 10-12, 미팅시간 9-13 (완전히 덮음)
        val freeTimes = listOf(createSchedule(10, 12))
        val meetings = listOf(createSchedule(9, 13))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유 시간이 모두 덮였으므로 빈 리스트가 반환되어야 함
        assertTrue(result.isEmpty())
    }

    @Test
    fun `미팅이 앞부분만 겹침`() {
        // 자유시간 10-14, 미팅시간 9-12 (앞부분 겹침)
        val freeTimes = listOf(createSchedule(10, 14))
        val meetings = listOf(createSchedule(9, 12))

        val result = excludeOverlap(freeTimes, meetings)

        // 남은 자유시간 12-14
        assertEquals(listOf(createSchedule(12, 14)), result)
    }

    @Test
    fun `미팅이 뒷부분만 겹침`() {
        // 자유시간 10-14, 미팅시간 12-14 (뒷부분 겹침)
        val freeTimes = listOf(createSchedule(10, 14))
        val meetings = listOf(createSchedule(12, 15))

        val result = excludeOverlap(freeTimes, meetings)

        // 남은 자유시간 10-12
        assertEquals(listOf(createSchedule(10, 12)), result)
    }

    @Test
    fun `미팅이 자유시간 중간을 겹침`() {
        // 자유시간 10-14, 미팅시간 11-13 (중간 겹침)
        val freeTimes = listOf(createSchedule(10, 14))
        val meetings = listOf(createSchedule(11, 13))

        val result = excludeOverlap(freeTimes, meetings)

        // 남은 자유시간 10-11, 13-14
        assertEquals(listOf(createSchedule(10, 11), createSchedule(13, 14)), result)
    }

    @Test
    fun `겹침 없는 다수의 자유시간`() {
        // 자유시간 9-11, 13-15, 미팅시간 11-13 (미팅이 두 자유시간 사이)
        val freeTimes = listOf(createSchedule(9, 11), createSchedule(13, 15))
        val meetings = listOf(createSchedule(11, 13))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유 시간이 미팅과 겹치지 않으므로 9-11, 13-15 시간이 그대로 유지되어야 함
        assertEquals(listOf(createSchedule(9, 11), createSchedule(13, 15)), result)
    }

    @Test
    fun `두 미팅이 하나의 자유시간과 겹침`() {
        // 자유시간 9-16, 미팅시간 10-12, 14-15 (중간에 빈 시간 있음)
        val freeTimes = listOf(createSchedule(9, 16))
        val meetings = listOf(createSchedule(10, 12), createSchedule(14, 15))

        val result = excludeOverlap(freeTimes, meetings)

        // 남은 자유시간 9-10, 12-14, 15-16
        assertEquals(listOf(createSchedule(9, 10), createSchedule(12, 14), createSchedule(15, 16)), result)
    }

    @Test
    fun `자유시간 두 개, 미팅 두 개, 겹침 없음`() {
        // 자유시간 9-11, 13-15, 미팅시간 16-18, 19-20 (전혀 겹치지 않음)
        val freeTimes = listOf(createSchedule(9, 11), createSchedule(13, 15))
        val meetings = listOf(createSchedule(16, 18), createSchedule(19, 20))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유 시간이 모두 유지되어야 함
        assertEquals(listOf(createSchedule(9, 11), createSchedule(13, 15)), result)
    }

    @Test
    fun `자유시간 두 개, 미팅 두 개, 앞뒤로 겹침`() {
        // 자유시간 10-12, 13-17, 미팅시간 9-11, 16-18 (앞뒤로 겹침)
        val freeTimes = listOf(createSchedule(10, 12), createSchedule(13, 17))
        val meetings = listOf(createSchedule(11, 14), createSchedule(15, 16))

        val result = excludeOverlap(freeTimes, meetings)

        // 결과: 남은 자유시간 11-12, 13-14
        assertEquals(listOf(createSchedule(10, 11), createSchedule(14, 15), createSchedule(16, 17)), result)
    }

    @Test
    fun `자유시간이 미팅 사이에 끼어 있음`() {
        // 자유시간 13-14, 미팅 12-13, 14-15 (자유시간이 미팅 사이에 끼어있음)
        val freeTimes = listOf(createSchedule(13, 14))
        val meetings = listOf(createSchedule(12, 13), createSchedule(14, 15))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유시간이 미팅에 걸리지 않으므로 그대로 유지
        assertEquals(listOf(createSchedule(13, 14)), result)
    }

    @Test
    fun `하나의 미팅이 두 자유시간에 걸쳐 있음`() {
        // 자유시간 9-11, 13-15, 미팅 10-14 (한 미팅이 두 자유시간에 겹침)
        val freeTimes = listOf(createSchedule(9, 11), createSchedule(13, 15))
        val meetings = listOf(createSchedule(10, 14))

        val result = excludeOverlap(freeTimes, meetings)

        // 남은 자유시간 9-10, 14-15
        assertEquals(listOf(createSchedule(9, 10), createSchedule(14, 15)), result)
    }

    @Test
    fun `겹치지 않는 자유시간과 미팅 경계`() {
        // 자유시간 9-11, 미팅 11-13 (경계가 맞닿아 있지만 겹치지 않음)
        val freeTimes = listOf(createSchedule(9, 11))
        val meetings = listOf(createSchedule(11, 13))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유시간 9-11 그대로 유지
        assertEquals(listOf(createSchedule(9, 11)), result)
    }

    @Test
    fun `자유시간과 미팅 경계가 일치`() {
        // 자유시간 10-12, 미팅 10-12 (시작 시간과 끝나는 시간이 정확히 일치)
        val freeTimes = listOf(createSchedule(10, 12))
        val meetings = listOf(createSchedule(10, 12))

        val result = excludeOverlap(freeTimes, meetings)

        // 자유시간이 미팅에 완전히 덮였으므로 빈 리스트가 반환되어야 함
        assertTrue(result.isEmpty())
    }
}
