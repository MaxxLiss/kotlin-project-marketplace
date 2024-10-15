package org.example.kotlinprojectmarketplace.database.dto.auth

data class AuthResponse(
    val message: String,
)

object AuthResponseMessage {
    const val SUCCESS_REGISTRATION = "Successfully registered"
    const val SUCCESS_LOGIN = "Successfully logged in"
}