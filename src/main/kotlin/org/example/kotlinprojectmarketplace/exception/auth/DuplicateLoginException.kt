package org.example.kotlinprojectmarketplace.exception.auth

import org.example.kotlinprojectmarketplace.exception.auth.AuthException

class DuplicateLoginException : AuthException() {
    override val message: String = "Duplicated login"
}