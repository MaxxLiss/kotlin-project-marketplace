package org.example.kotlinprojectmarketplace.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class UnauthorizedException(
    message: String = "Unauthorized",
    statusCode: HttpStatus = HttpStatus.UNAUTHORIZED,
    happenedAt: LocalDateTime = LocalDateTime.now(),
) : ApiException(message, statusCode, happenedAt)