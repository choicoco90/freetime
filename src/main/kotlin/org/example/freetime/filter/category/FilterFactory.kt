package org.example.freetime.filter.category

import org.springframework.stereotype.Component

@Component
class FilterFactory(
    val authFilter: AuthFilter,
    val noAuthFilter: NoAuthFilter
) {
    fun findFilter(path: String): PathFilter {
        if(AUTH_PASS_PATH.any { path.startsWith(it) }){
            return noAuthFilter
        }
        return authFilter
    }
    companion object{
        val AUTH_PASS_PATH = listOf(
            "/users/auth",
            "/hello",
            "/swagger-ui",
            "/v3/api-docs"
        )
    }
}
