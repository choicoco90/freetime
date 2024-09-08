package org.example.freetime.utils

import java.time.format.DateTimeFormatter

object Constants {
    const val BEARER = "Bearer "
    val DEFAULT_DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
}
