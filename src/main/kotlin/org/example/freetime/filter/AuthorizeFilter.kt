package org.example.freetime.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
import org.example.freetime.exception.AuthException
import org.example.freetime.exception.ErrorCode
import org.example.freetime.exception.ErrorResponse
import org.example.freetime.filter.category.FilterFactory
import org.example.freetime.utils.DEFAULT_MAPPER
import org.example.freetime.utils.logger
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.io.OutputStream

@Order(2)
@Component
class AuthorizeFilter(
    val filterFactory: FilterFactory,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val path = request.servletPath
            logger().info("[${request.method}] $path")
            val filter = filterFactory.findFilter(path)
            filter.handleRequest(request, response, filterChain)
        } catch (e: AuthException) {
            sendErrorResponse(response, e.getErrorCode())
        }

    }

    @Throws(IOException::class)
    private fun sendErrorResponse(response: HttpServletResponse, errorCode: ErrorCode) {
        response.status = errorCode.httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val errorResponse = ErrorResponse(errorCode)
        response.outputStream.addResponse(errorResponse)
    }
    private fun OutputStream.addResponse(errorResponse: ErrorResponse){
        DEFAULT_MAPPER.writeValue(this, errorResponse)
    }
}
