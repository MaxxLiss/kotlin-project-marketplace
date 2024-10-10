package org.example.kotlinprojectmarketplace.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class AuthException(
    message: String = "Something went wrong during the authentication",
    statusCode: HttpStatus = HttpStatus.BAD_REQUEST,
    happenedAt: LocalDateTime = LocalDateTime.now(),
) : ApiException(message, statusCode, happenedAt)

object AuthExceptionMessage {
    const val DUPLICATED_LOGIN = "Duplicated login"
    const val INVALID_CREDENTIALS = "Invalid credentials"
}