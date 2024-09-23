package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.enums.NoticeChannel
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode

@Schema(description = "사용자 등록 요청")
data class UserCreateRequest(
    @Schema(description = "사용자 이름", example = "홍길동", nullable = false)
    val name: String,
    @Schema(description = "사용자 이메일", example = "test@test.com", nullable = false)
    val email: String,
    @Schema(description = "사용자 비밀번호", example = "1234", nullable = false)
    val password: String,
    @Schema(description = "사용자 핸드폰", example = "01011111111", nullable = true)
    val phone: String?,
    @Schema(description = "사용자 알림 채널", example = "EMAIL", nullable = false)
    val preferredNoticeChannel: NoticeChannel
){
    fun validate(){
        if(phone == null && preferredNoticeChannel == NoticeChannel.SMS){
            throw BizException(ErrorCode.CANNOT_USE_SMS)
        }
    }
}
