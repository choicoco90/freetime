package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.enum.NoticeChannel

@Schema(description = "사용자 수정 요청 (이메일 수정 불가")
data class UserUpdateRequest(
    @Schema(description = "사용자 이름", example = "홍길동", nullable = false)
    val name: String,
    @Schema(description = "사용자 핸드폰", example = "010-1111-1111", nullable = true)
    val phone: String?,
    @Schema(description = "사용자 알림 채널", example = "EMAIL", nullable = false)
    val preferredNoticeChannel: NoticeChannel,
){
    fun validate(){
        if(phone == null && preferredNoticeChannel == NoticeChannel.SMS){
            throw RuntimeException("핸드폰 번호가 없어 SMS 알림 채널을 선택할 수 없습니다.")
        }
    }
}
