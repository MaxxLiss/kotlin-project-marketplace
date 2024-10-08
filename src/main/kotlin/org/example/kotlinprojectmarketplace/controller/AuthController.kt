package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    private lateinit var authService: AuthService

    // todo нормальный Response. Возможно, надо переработать AuthResponse
    @PostMapping("/register")
    fun register(@RequestBody authRequest: AuthRequest): ResponseEntity<String> = try {
        authService.register(authRequest)
        ResponseEntity.ok(AuthResponseMessage.SUCCESS_REGISTRATION.text)
    } catch (ex: Exception) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<String> = try {
        authService.login(authRequest)
        ResponseEntity.ok(AuthResponseMessage.SUCCESS_LOGIN.text)
    } catch (ex: Exception) {
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

}