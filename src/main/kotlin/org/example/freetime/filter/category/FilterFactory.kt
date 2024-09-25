package org.example.freetime.filter.category

import org.springframework.stereotype.Component

@Component
class FilterFactory(
    val authFilter: AuthFilter,
    val noAuthFilter: NoAuthFilter
) {
    fun findFilter(path: String): PathFilter {
        if(path.startsWith("/users/auth")){
            return noAuthFilter
        }
        if(AUTH_PATH.any { path.startsWith(it) }){
            return authFilter
        }
        return noAuthFilter
    }
    companion object{
        val AUTH_PATH = listOf(
            "/users",
            "/groups",
            "/meetings",
            "/proposals",
            "/group-meetings",
            "/free-time"
        )
    }
}
