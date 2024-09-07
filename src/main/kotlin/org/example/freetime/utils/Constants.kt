package org.example.freetime.utils

import java.time.format.DateTimeFormatter

object Constants {
    val DEFAULT_DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val DEFAULT_DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
}
