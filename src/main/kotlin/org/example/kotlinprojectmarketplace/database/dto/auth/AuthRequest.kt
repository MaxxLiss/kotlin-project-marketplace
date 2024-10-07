package org.example.kotlinprojectmarketplace.database.dto.auth

data class AuthRequest(
    val login: String,
    val password: String
)