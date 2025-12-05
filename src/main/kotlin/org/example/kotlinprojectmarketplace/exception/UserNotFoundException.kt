package org.example.kotlinprojectmarketplace.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class UserNotFoundException(
    message: String = "Something went wrong",
    statusCode: HttpStatus = HttpStatus.NOT_FOUND,
    happenedAt: LocalDateTime = LocalDateTime.now(),
): ApiException(message, statusCode, happenedAt)