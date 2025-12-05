package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.AuthResponse

interface AuthService {
    fun register(authRequest: AuthRequest): AuthResponse

    fun login(authRequest: AuthRequest): AuthResponse
}