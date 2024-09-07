package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "비밀번호 찾기 응답")
data class UserResetPasswordResponse(
    @Schema(description = "사용자 이메일")
    val email: String,
    @Schema(description = "사용자 이름")
    val name: String,
    @Schema(description = "암호화 키")
    val secret: String,
)
