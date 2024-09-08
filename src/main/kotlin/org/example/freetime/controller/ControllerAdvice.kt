package org.example.freetime.controller

import jakarta.servlet.http.HttpServletRequest
import org.example.freetime.exception.BizException
import org.example.freetime.exception.ErrorCode
import org.example.freetime.exception.ErrorResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(BizException::class)
    fun handleBizException(request: HttpServletRequest, e: BizException): ResponseEntity<Any> {
        e.printStackTrace()
        val message = e.message.ifEmpty { e.errorCode.message }
        return createResponseEntity(e.errorCode, message)
    }

    @ExceptionHandler(Exception::class)
    fun handleBadRequestException(request: HttpServletRequest, exception: Exception): ResponseEntity<Any> {
        exception.printStackTrace()
        val errorCode = ErrorCode.UNEXPECTED
        val response = ErrorResponse(errorCode, exception.message?.ifEmpty { errorCode.message } ?: errorCode.message)
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body<Any>(response)
    }

    private fun createResponseEntity(errorCode: ErrorCode, message: String): ResponseEntity<Any> {
        return ResponseEntity
            .status(errorCode.httpStatus)
            .body(ErrorResponse(errorCode, message))
    }

}
