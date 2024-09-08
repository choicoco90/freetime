package org.example.freetime.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun api(): OpenAPI {
        // Bearer 토큰 설정
        val bearerScheme: SecurityScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP) // HTTP 타입으로 변경
            .scheme("bearer") // Bearer 토큰 스키마 지정
            .bearerFormat("JWT") // JWT 포맷으로 설정 (필요 시)

        // 보안 요구 사항
        val securityRequirement: SecurityRequirement = SecurityRequirement()
            .addList("Bearer Token")

        return OpenAPI()
            .components(Components().addSecuritySchemes("Bearer Token", bearerScheme))
            .addSecurityItem(securityRequirement)
    }
}
