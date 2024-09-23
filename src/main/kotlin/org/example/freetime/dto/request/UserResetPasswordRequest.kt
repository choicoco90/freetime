package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "비밀번호 찾기 요청")
data class UserResetPasswordRequest(
    @Schema(description = "사용자 이메일")
    val email: String,
    @Schema(description = "사용자 이름")
    val name: String,
    @Schema(description = "새로운 비밀번호")
    val newPassword: String,
)
