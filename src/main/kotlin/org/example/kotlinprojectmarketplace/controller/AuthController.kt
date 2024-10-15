package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    @Autowired
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(@RequestBody authRequest: AuthRequest) : ResponseEntity<AuthResponse> {
        authService.register(authRequest)

        return ResponseEntity.status(HttpStatus.CREATED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(AuthResponse(AuthResponseMessage.SUCCESS_REGISTRATION))
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        authService.login(authRequest)
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(AuthResponse(AuthResponseMessage.SUCCESS_LOGIN))
    }
}