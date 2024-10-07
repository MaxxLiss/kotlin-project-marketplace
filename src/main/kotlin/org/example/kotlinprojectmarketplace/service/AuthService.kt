package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse

interface AuthService {
    fun register(authRequest: AuthRequest): AuthResponse

    fun login(authRequest: AuthRequest): AuthResponse
}