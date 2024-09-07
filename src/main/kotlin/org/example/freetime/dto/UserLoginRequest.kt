package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 로그인 요청")
data class UserLoginRequest(
    @Schema(description = "사용자 이메일")
    val email: String,
    @Schema(description = "사용자 비밀번호")
    val password: String,
)
