package org.example.freetime.exception

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ErrorResponse(
    val code: String,
    val message: String,
    val status: Int
) {
    constructor(errorCode: ErrorCode, message: String) : this(
        errorCode.code,
        message,
        errorCode.httpStatus.value()
    )
    constructor(errorCode: ErrorCode) : this(
        errorCode.code,
        errorCode.message,
        errorCode.httpStatus.value()
    )
}
