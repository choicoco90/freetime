package org.example.freetime.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.example.freetime.entities.UserEntity
import org.example.freetime.enum.NoticeChannel

@Schema(description = "사용자 정보 응답")
data class UserResponse(
    @Schema(description = "사용자 이름")
    val name: String,
    @Schema(description = "사용자 이메일")
    val email: String,
    @Schema(description = "사용자 핸드폰 번호")
    val phone: String? = null,
    @Schema(description = "사용자 알림 채널")
    val preferredNoticeChannel: NoticeChannel,
){
    companion object {
        fun from(entity: UserEntity): UserResponse {
            return UserResponse(
                name = entity.name,
                email = entity.email,
                phone = entity.phone,
                preferredNoticeChannel = entity.preferredNoticeChannel,
            )
        }
    }
}
