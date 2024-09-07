package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.enum.NoticeChannel

@Schema(description = "사용자 등록 요청")
data class UserCreateRequest(
    @Schema(description = "사용자 이름", example = "홍길동", nullable = false)
    val name: String,
    @Schema(description = "사용자 이메일", example = "test@test.com", nullable = false)
    val email: String,
    @Schema(description = "사용자 비밀번호", example = "password", nullable = false)
    val password: String,
    @Schema(description = "사용자 핸드폰", example = "010-1111-1111", nullable = true)
    val phone: String?,
    @Schema(description = "사용자 알림 채널", example = "EMAIL", nullable = false)
    val preferredNoticeChannel: NoticeChannel,
    @Schema(description = "주간 빈 시간 설정", nullable = false)
    val weeklyFreeTime: FreeTimeWeeklyRequest
){
    fun validate(){
        if(phone == null && preferredNoticeChannel == NoticeChannel.SMS){
            throw RuntimeException("핸드폰 번호가 없어 SMS 알림 채널을 선택할 수 없습니다.")
        }
    }
}
