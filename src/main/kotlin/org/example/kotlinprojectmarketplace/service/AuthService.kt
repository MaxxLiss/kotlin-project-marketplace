package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest

interface AuthService {
    fun register(authRequest: AuthRequest)

    fun login(authRequest: AuthRequest)
}