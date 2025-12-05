package org.example.kotlinprojectmarketplace.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class AccessDeniedException(
    message: String = "Access is denied",
    statusCode: HttpStatus = HttpStatus.FORBIDDEN,
    happenedAt: LocalDateTime = LocalDateTime.now(),
): ApiException(message, statusCode, happenedAt)