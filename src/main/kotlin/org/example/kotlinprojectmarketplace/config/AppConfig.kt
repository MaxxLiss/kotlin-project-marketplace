package org.example.kotlinprojectmarketplace.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@EnableWebMvc
@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class AppConfig {
    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper().apply {
        findAndRegisterModules()
    }
}