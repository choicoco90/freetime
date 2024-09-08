package org.example.freetime.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import org.example.freetime.domain.Token
import org.example.freetime.enums.TokenType
import org.example.freetime.dto.UserCreateRequest
import org.example.freetime.dto.UserResetPasswordRequest
import org.example.freetime.dto.UserResetPasswordResponse
import org.example.freetime.dto.UserUpdateRequest
import org.example.freetime.entities.UserEntity
import org.example.freetime.exception.AuthException
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import org.example.freetime.repository.DailyFreeTimeRepository
import org.example.freetime.repository.MeetingRepository
import org.example.freetime.repository.ProposalRepository
import org.example.freetime.repository.UserRepository
import org.example.freetime.repository.WeeklyFreeTimeRepository
import org.example.freetime.utils.Constants.BEARER
import org.example.freetime.utils.FreeTimeUtils
import org.example.freetime.utils.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Service
class UserService(
    @Value("\${jwt.secret.key}") private val secretKey: String,
    val userRepository: UserRepository,
    val weeklyFreeTimeRepository: WeeklyFreeTimeRepository,
    val dailyFreeTimeRepository: DailyFreeTimeRepository,
) {
    private final var algorithm: Algorithm = Algorithm.HMAC256(secretKey.toByteArray())
    var jwtVerifier: com.auth0.jwt.JWTVerifier = JWT.require(algorithm).build()

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserEntity>{
        return userRepository.findAll().also {
            logger().info("User list size: ${it.size}")
        }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserEntity = userRepository.findById(id).orElseThrow { throw BizException(ErrorCode.USER_NOT_FOUND) }

    @Transactional(readOnly = false)
    fun deleteUserById(id: Long){
        val user = getUserById(id)
        weeklyFreeTimeRepository.deleteByUserId(id)
        dailyFreeTimeRepository.deleteByUserId(id)
        user.deleteUserInfo()
        userRepository.save(user)
    }

    @Transactional(readOnly = false)
    fun createUserAndRegisterDefaultSchedule(request: UserCreateRequest): Token {
        request.validate()
        if(userRepository.existsByEmail(request.email)) {
            throw BizException(ErrorCode.USER_ALREADY_EXISTS)
        }
        val user = UserEntity(
            name = request.name,
            email = request.email,
            password = request.password,
            phone = request.phone
        )
        val saved = userRepository.save(user)
        val command = request.weeklyFreeTime.toCommand()
        val weeklyFreeTime = command.toEntity(saved.id)
        weeklyFreeTimeRepository.save(weeklyFreeTime)
        return createToken(saved.id)
    }
    @Transactional(readOnly = false)
    fun updateUser(id: Long, request: UserUpdateRequest) {
        val user = getUserById(id)
        user.updateUserInfo(request)
        userRepository.save(user)
    }
    @Transactional(readOnly = false)
    fun login(email: String, password: String): Token {
        val user = userRepository.findByEmail(email) ?: throw BizException(ErrorCode.USER_NOT_FOUND)
        if (user.password != password) {
            throw BizException(ErrorCode.USER_PASSWORD_MISMATCH)
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
        val user = userRepository.findByEmailAndName(request.email, request.name) ?: throw BizException(ErrorCode.USER_NOT_FOUND)
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

    fun authenticate(authorization: String?): Pair<String, Long> {
        val token = extractToken(authorization)
        try {
            val decodedJWT = jwtVerifier.verify(token)
            val crewId = decodedJWT.subject.toLong()
            return Pair(token, crewId)
        } catch (jwtDecodeException: JWTDecodeException) {
            throw AuthException(ErrorCode.TOKEN_VERIFICATION_ERROR)
        } catch (jwtDecodeException: SignatureVerificationException) {
            throw AuthException(ErrorCode.TOKEN_VERIFICATION_ERROR)
        } catch (tokenExpiredException: TokenExpiredException) {
            throw AuthException(ErrorCode.TOKEN_EXPIRED_ERROR)
        }

    }
    private fun extractToken(authorization: String?): String {
        return if (authorization != null && authorization.startsWith(BEARER)) {
            authorization.substring(BEARER.length)
        } else {
            throw AuthException(ErrorCode.TOKEN_MISSING)
        }
    }

    private fun createJwtString(userId: Long, tokenType: TokenType): String {
        return JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + tokenType.expire))
            .sign(algorithm)
    }

}
