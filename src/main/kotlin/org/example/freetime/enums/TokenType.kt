package org.example.freetime.enums

enum class TokenType(val expire: Long) {
    ACCESS(6 * 60 * 60 * 1000L), // 6시간
    REFRESH(60 * 24 * 60 * 60 * 1000L) // 60일
}
