package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "그룹 생성 요청")
data class CreateGroupRequest(
    @Schema(description = "그룹 이름")
    val groupName: String,
    @Schema(description = "그룹 설명")
    val description: String,
    @Schema(description = "그룹 유저 아이디 목록")
    val userIds: List<Long>
)
