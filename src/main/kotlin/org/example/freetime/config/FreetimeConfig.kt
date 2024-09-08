package org.example.freetime.config

import org.example.freetime.utils.DEFAULT_MAPPER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FreetimeConfig {
    @Bean
    fun objectMapper() = DEFAULT_MAPPER
}
