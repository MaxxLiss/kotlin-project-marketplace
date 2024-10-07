package org.example.kotlinprojectmarketplace.exception.auth

open class AuthException : Exception() {
    override val message: String = "Smth went wrong during the authentication"
}