package org.example.kotlinprojectmarketplace.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class UserInfoEnrichException(
    message: String = "Something went wrong during update user information",
    statusCode: HttpStatus = HttpStatus.BAD_REQUEST,
    happenedAt: LocalDateTime = LocalDateTime.now(),
): ApiException(message, statusCode, happenedAt)

object UserInfoEnrichExceptionMessage{
    const val DATA_CONFLICT = "Data Conflict"
}