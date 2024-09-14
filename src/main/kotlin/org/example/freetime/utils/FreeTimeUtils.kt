package org.example.freetime.utils

import org.example.freetime.domain.Schedule
import org.example.freetime.enums.OverlapType

object FreeTimeUtils {
    private const val DELIMITER = ":"

    fun encryptResetPasswordString(userId: Long, newPassword: String): String {
        val data = listOf(userId, newPassword).joinToString(DELIMITER)
        return EncryptUtils.encrypt(data)
    }

    fun decryptResetPasswordString(resetPasswordString: String): Pair<Long, String> {
        val decrypted = EncryptUtils.decrypt(resetPasswordString)
        val split = decrypted.split(DELIMITER)
        return Pair(split[0].toLong(), split[1])
    }

    fun excludeOverlap(freeTimes: List<Schedule>, meetings: List<Schedule>): List<Schedule> {
        val finalFreeTimes = mutableListOf<Schedule>()

        freeTimes.forEach { freeTime ->
            var current = freeTime
            meetings.forEach { meeting ->

                val overlap = current.determineOverlapType(meeting)

                when (overlap) {
                    OverlapType.NO_OVERLAP -> {
                        // 미팅과 자유 시간이 겹치지 않는 경우 아무 처리도 하지 않음
                    }
                    OverlapType.FRONT_OVERLAP -> {
                        // 미팅이 자유 시간의 앞부분을 덮는 경우, 남은 시간 업데이트
                        current = Schedule(meeting.end, current.end)
                    }
                    OverlapType.BACK_OVERLAP -> {
                        // 미팅이 자유 시간의 뒷부분을 덮는 경우, 남은 시간 업데이트
                        current = Schedule(current.start, meeting.start)
                    }
                    OverlapType.MIDDLE_OVERLAP -> {
                        // 미팅이 자유 시간의 중간을 덮는 경우
                        val firstPart = Schedule(current.start, meeting.start)
                        val secondPart = Schedule(meeting.end, current.end)

                        // 앞부분을 결과 리스트에 추가
                        finalFreeTimes.add(firstPart)

                        // current를 나머지 시간으로 설정
                        current = secondPart
                    }
                    OverlapType.ALL_OVERLAP -> {
                        // 미팅이 자유 시간을 완전히 덮는 경우, 빈 리스트 반환
                        current = Schedule.empty()
                    }
                }
            }
            if(!current.isEmpty()){
                finalFreeTimes.add(current)
            }
        }

        return finalFreeTimes
    }


    private fun Schedule.determineOverlapType(meeting: Schedule): OverlapType {
        return when {
            this.end <= meeting.start || this.start >= meeting.end -> {
                // 겹치지 않는 경우
                OverlapType.NO_OVERLAP
            }
            meeting.start <= this.start && meeting.end >= this.end -> {
                // 미팅이 자유 시간을 완전히 덮는 경우
                OverlapType.ALL_OVERLAP
            }
            meeting.start <= this.start && meeting.end < this.end -> {
                // 미팅이 자유 시간의 앞부분을 덮는 경우
                OverlapType.FRONT_OVERLAP
            }
            meeting.start > this.start && meeting.end >= this.end -> {
                // 미팅이 자유 시간의 뒷부분을 덮는 경우
                OverlapType.BACK_OVERLAP
            }
            meeting.start > this.start && meeting.end < this.end -> {
                // 미팅이 자유 시간의 중간을 덮는 경우
                OverlapType.MIDDLE_OVERLAP
            }
            else -> {
                // 처리되지 않은 경우, 안전 장치
                throw IllegalArgumentException("Unexpected overlap case")
            }
        }
    }
}
