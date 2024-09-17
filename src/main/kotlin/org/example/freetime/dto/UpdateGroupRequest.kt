package org.example.freetime.dto

data class UpdateGroupRequest(
    val groupId: Long,
    val groupName: String,
    val groupLeader: Long,
    val description: String,
)
