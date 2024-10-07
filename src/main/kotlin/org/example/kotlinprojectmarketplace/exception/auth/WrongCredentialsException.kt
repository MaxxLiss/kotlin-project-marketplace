package org.example.kotlinprojectmarketplace.exception.auth

class WrongCredentialsException : AuthException() {
    override val message: String = "Wrong credentials"
}