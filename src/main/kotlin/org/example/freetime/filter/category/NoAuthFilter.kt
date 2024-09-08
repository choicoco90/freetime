package org.example.freetime.filter.category

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.freetime.service.UserService
import org.springframework.stereotype.Component

@Component
class NoAuthFilter: PathFilter {
    override fun handleRequest(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        filterChain.doFilter(request, response)
    }
}
