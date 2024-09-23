package org.example.freetime.dto.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "그룹 수정 요청")
data class UpdateGroupRequest(
    @Schema(description = "그룹 이름")
    val groupName: String,
    @Schema(description = "그룹 리더 아이디")
    val groupLeader: Long,
    @Schema(description = "그룹 설명")
    val description: String,
)
