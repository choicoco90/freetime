package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.enums.NoticeChannel
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode

@Schema(description = "사용자 수정 요청 (이메일 수정 불가")
data class UserUpdateRequest(
    @Schema(description = "사용자 이름", example = "홍길동", nullable = false)
    val name: String,
    @Schema(description = "사용자 핸드폰", example = "01011111111", nullable = true)
    val phone: String?,
    @Schema(description = "사용자 알림 채널", example = "EMAIL", nullable = false)
    val preferredNoticeChannel: NoticeChannel,
)
