package org.example.freetime.exception

class BizException(val errorCode: ErrorCode) : RuntimeException(errorCode.message) {
    override var message: String = ""
}
