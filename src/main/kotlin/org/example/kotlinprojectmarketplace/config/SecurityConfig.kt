package org.example.kotlinprojectmarketplace.config

import org.example.kotlinprojectmarketplace.security.JwtAuthFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val corsConfig: CorsConfigurationSource,
) {
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter
    ): DefaultSecurityFilterChain = http
        .cors { it.configurationSource(corsConfig) }
        .csrf { it.disable() }
        .authorizeHttpRequests {
            it
                .requestMatchers("error/").permitAll()
                .requestMatchers(HttpMethod.POST, "api/auth/**").permitAll()
        }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        .authenticationManager(authManager(http))
        .build()

    //todo что это :)
    @Bean
    fun authManager(http: HttpSecurity): AuthenticationManager? {
        val builder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        builder.inMemoryAuthentication()
        return builder.build()
    }
}