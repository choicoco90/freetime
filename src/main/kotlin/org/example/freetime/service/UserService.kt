package org.example.freetime.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import org.example.freetime.domain.Token
import org.example.freetime.domain.TokenType
import org.example.freetime.dto.UserCreateRequest
import org.example.freetime.dto.UserResetPasswordRequest
import org.example.freetime.dto.UserResetPasswordResponse
import org.example.freetime.dto.UserUpdateRequest
import org.example.freetime.entities.UserEntity
import org.example.freetime.repository.UserRepository
import org.example.freetime.utils.FreeTimeUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class UserService(
    @Value("\${jwt.secret.key}") private val secretKey: String,
    val userRepository: UserRepository,
    val freeTimeService: FreeTimeService
) {
    private final var algorithm: Algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserEntity = userRepository.findById(id).orElseThrow { throw RuntimeException("User not found") }

    @Transactional(readOnly = false)
    fun deleteUserById(id: Long) = userRepository.deleteById(id)

    @Transactional(readOnly = false)
    fun createUserAndRegisterDefaultSchedule(request: UserCreateRequest): Token {
        request.validate()
        val user = UserEntity(
            name = request.name,
            email = request.email,
            password = request.password,
            phone = request.phone
        )
        val saved = userRepository.save(user)
        val command = request.weeklyFreeTime.toCommand()
        freeTimeService.createWeeklyFreeTimes(saved.id, command)
        return createToken(saved.id)
    }
    @Transactional(readOnly = false)
    fun updateUser(id: Long, request: UserUpdateRequest) {
        request.validate()
        val user = getUserById(id)
        user.updateUserInfo(request)
        userRepository.save(user)
    }
    @Transactional(readOnly = false)
    fun login(email: String, password: String): Token {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("User not found")
        if (user.password != password) {
            throw RuntimeException("Password not matched")
        }
        return createToken(user.id)
    }
    @Transactional(readOnly = false)
    fun createToken(userId: Long): Token {
        return Token(
            accessToken = createJwtString(userId, TokenType.ACCESS),
            refreshToken = createJwtString(userId, TokenType.REFRESH)
        )
    }
    @Transactional(readOnly = false)
    fun resetPasswordConfirmSecret(request: UserResetPasswordRequest): UserResetPasswordResponse {
        val user = userRepository.findByEmailAndName(request.email, request.name) ?: throw RuntimeException("User not found")
        // send email with secret code
        val secret = FreeTimeUtils.encryptResetPasswordString(user.id, request.newPassword)
        return UserResetPasswordResponse(
            email = user.email,
            name = user.name,
            secret = secret
        )
    }
    @Transactional(readOnly = false)
    fun confirmAndResetPassword(secret: String) {
        // find user by secret code and reset password
        val (userId, password) = FreeTimeUtils.decryptResetPasswordString(secret)
        val user = getUserById(userId)
        user.resetPassword(password)
        userRepository.save(user)
    }

    private fun createJwtString(userId: Long, tokenType: TokenType): String {
        return JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + tokenType.expire))
            .sign(algorithm)
    }

}
