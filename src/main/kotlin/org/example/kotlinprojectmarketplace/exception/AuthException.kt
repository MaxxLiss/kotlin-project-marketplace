package org.example.kotlinprojectmarketplace.exception

class AuthException(
    override val message: String = "Something went wrong during the authentication",
) : AppException()

enum class AuthExceptionMessage(val text: String) {
    DUPLICATED_LOGIN("Duplicated login"),
    WRONG_CREDENTIALS("Wrong credentials"),
}