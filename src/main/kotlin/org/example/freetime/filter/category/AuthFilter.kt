package org.example.freetime.filter.category

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.freetime.service.UserService
import org.example.freetime.utils.logger
import org.springframework.stereotype.Component

@Component
class AuthFilter(
    val userService: UserService
): PathFilter {
    override fun handleRequest(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorization = request.getHeader("Authorization")
        logger().info("Authorization: $authorization")
        val (token, userId) = userService.authenticate(authorization)
        logger().info("userId: $userId")
        request.setAttribute("userId", userId)
        request.setAttribute("token", token)
        filterChain.doFilter(request, response)
    }
}
