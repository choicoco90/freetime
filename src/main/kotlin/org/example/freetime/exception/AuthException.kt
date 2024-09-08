package org.example.freetime.exception

class AuthException: RuntimeException {
    private val errorCode: ErrorCode
    override var message: String = ""

    constructor(errorCode: ErrorCode) : super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(
        errorCode: ErrorCode,
        message: String
    ) : super(message) {
        this.errorCode = errorCode
        this.message = message
    }
    fun getErrorCode(): ErrorCode {
        return errorCode
    }
}
