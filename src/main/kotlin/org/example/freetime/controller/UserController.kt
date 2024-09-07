package org.example.freetime.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.freetime.dto.TokenResponse
import org.example.freetime.dto.UserCreateRequest
import org.example.freetime.dto.UserLoginRequest
import org.example.freetime.dto.UserResetPasswordRequest
import org.example.freetime.dto.UserResetPasswordResponse
import org.example.freetime.dto.UserResponse
import org.example.freetime.dto.UserUpdateRequest
import org.example.freetime.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@Tag(name = "사용자 관리 API")
class UserController(
    val userService: UserService
) {
    @Operation(description = "사용자 등록")
    @PostMapping("/auth/register")
    fun register(
        @RequestBody request: UserCreateRequest
    ): TokenResponse {
        val token = userService.createUserAndRegisterDefaultSchedule(request)
        return TokenResponse.from(token)
    }

    @Operation(description = "사용자 로그인")
    @PostMapping("/auth/login")
    fun login(
        @RequestBody request: UserLoginRequest
    ): TokenResponse {
        val token = userService.login(request.email, request.password)
        return TokenResponse.from(token)
    }

    @Operation(description = "비밀번호 찾기 문자열 생성 (암호화 키)")
    @PostMapping("/auth/password/reset")
    fun resetPasswordString(
        @RequestBody request: UserResetPasswordRequest
    ): UserResetPasswordResponse {
        return userService.resetPasswordConfirmSecret(request)
    }

    @Operation(description = "비밀번호 찾기 문자열 확인 및 비밀번호 재설정")
    @GetMapping("/auth/password/confirm")
    fun confirmAndResetPassword(
        @Schema(description = "암호화 키")
        @RequestParam secret: String,
    ) {
        userService.confirmAndResetPassword(secret)
    }

    @Operation(description = "사용자 정보 수정")
    @PutMapping("/update")
    fun update(
        @RequestAttribute userId: Long,
        @RequestBody request: UserUpdateRequest
    ) {
        userService.updateUser(userId, request)
    }

    @Operation(description = "토큰 갱신")
    @PostMapping("/refresh")
    fun refresh(
        @RequestAttribute userId: Long
    ): TokenResponse {
        val user = userService.getUserById(userId)
        return TokenResponse.from(userService.createToken(user.id))
    }

    @Operation(description = "사용자 삭제")
    @DeleteMapping("/delete")
    fun delete(
        @RequestAttribute userId: Long
    ) {
        userService.deleteUserById(userId)
    }

    @Operation(description = "사용자 조회")
    @GetMapping
    fun getUser(
        @RequestAttribute userId: Long
    ): UserResponse {
        return UserResponse.from(userService.getUserById(userId))
    }
}
