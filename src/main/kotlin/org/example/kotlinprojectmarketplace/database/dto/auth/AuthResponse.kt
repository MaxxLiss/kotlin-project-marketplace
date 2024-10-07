package org.example.kotlinprojectmarketplace.database.dto.auth

enum class AuthResponseMessage(val text: String) {
    SUCCESS_REGISTRATION("Successfully registered"),
    SUCCESS_LOGIN("Successfully logged in"),
}

data class AuthResponse(
    val message: AuthResponseMessage,
)