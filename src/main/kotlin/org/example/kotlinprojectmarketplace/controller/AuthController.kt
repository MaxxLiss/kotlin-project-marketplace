package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
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
    fun register(
        @RequestBody authRequest: AuthRequest
    ) : ResponseEntity<AuthResponse> = ResponseEntity
        .status(HttpStatus.CREATED)
        .contentType(MediaType.APPLICATION_JSON)
        .body(authService.register(authRequest))

    @PostMapping("/login")
    fun login(
        @RequestBody authRequest: AuthRequest
    ): ResponseEntity<AuthResponse> = ResponseEntity
        .status(HttpStatus.OK)
        .contentType(MediaType.APPLICATION_JSON)
        .body(authService.login(authRequest))
}