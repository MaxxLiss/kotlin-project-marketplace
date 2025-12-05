package org.example.kotlinprojectmarketplace.database.dto

data class AuthRequest(
    val login: String,
    val password: String
)