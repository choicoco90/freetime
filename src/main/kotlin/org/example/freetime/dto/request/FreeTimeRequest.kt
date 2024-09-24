package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.model.FreeTime

@Schema(description = "빈 시간 생성/수정 요청")
data class FreeTimeRequest(
    @Schema(description = "빈 시간 시작 시간 (시)", example = "6", nullable = false)
    val startHour: Int,
    @Schema(description = "빈 시간 시작 시간 (분)", example = "30", nullable = false)
    val startMinute: Int,
    @Schema(description = "빈 시간 종료 시간 (시)", example = "9", nullable = false)
    val endHour: Int,
    @Schema(description = "빈 시간 종료 시간 (분)", example = "0", nullable = false)
    val endMinute: Int
){
    fun toDomain(): FreeTime {
        validateTime()
        return FreeTime(startHour, startMinute, endHour, endMinute)
    }
    private fun validateTime() {
        val valueValid = startHour in 0..23 && startMinute in 0..59 && endHour in 0..23 && endMinute in 0..59
        val timeValid = startHour < endHour || (startHour == endHour && startMinute < endMinute)
        if (!valueValid){
            throw IllegalArgumentException("시간은 0~23, 분은 0~59 사이의 값이어야 합니다.")
        }
        if (!timeValid){
            throw IllegalArgumentException("시작 시간은 종료 시간보다 빨라야 합니다.")
        }
    }
}
