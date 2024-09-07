package org.example.freetime.domain

data class Token(
    val accessToken: String,
    val refreshToken: String,
)
