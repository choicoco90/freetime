package org.example.freetime.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.freetime.dto.request.UserUpdateRequest
import org.example.freetime.enums.NoticeChannel
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import java.util.UUID


@Entity
@Table(name = "users")
data class UserEntity(
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    var password: String,
    @Column(name = "phone", nullable = true)
    var phone: String?,
    @Column(name = "noticeChannel", nullable = false)
    @Enumerated(EnumType.STRING)
    var preferredNoticeChannel: NoticeChannel = NoticeChannel.EMAIL,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    fun updateUserInfo(request: UserUpdateRequest){
        this.name = request.name
        this.phone = request.phone
        if(request.preferredNoticeChannel == NoticeChannel.SMS && this.phone == null){
            throw BizException(ErrorCode.CANNOT_USE_SMS)
        }
        this.preferredNoticeChannel = request.preferredNoticeChannel
    }

    fun resetPassword(newPassword: String){
        this.password = newPassword
    }

    fun deleteUserInfo(){
        this.email = UUID.randomUUID().toString() + "@deleted.com"
        this.name = "탈퇴한 사용자"
        this.phone = "00000000000"
        this.preferredNoticeChannel = NoticeChannel.EMAIL
    }
}
