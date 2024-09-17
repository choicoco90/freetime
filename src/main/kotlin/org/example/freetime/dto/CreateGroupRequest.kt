package org.example.freetime.dto

data class CreateGroupRequest(
    val groupName: String,
    val description: String,
    val userIds: List<Long>
)
