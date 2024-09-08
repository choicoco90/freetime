package org.example.freetime.filter.category

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException

interface PathFilter {
    @Throws(ServletException::class, IOException::class)
    fun handleRequest(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain)
}
