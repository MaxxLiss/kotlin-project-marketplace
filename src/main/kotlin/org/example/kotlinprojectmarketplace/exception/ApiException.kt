package org.example.kotlinprojectmarketplace.exception

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonIgnoreProperties("cause", "stackTrace", "localizedMessage", "suppressed")
sealed class ApiException(
    override val message: String = "Something went wrong",

    val statusCode: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    val happenedAt: LocalDateTime = LocalDateTime.now(),

) : RuntimeException()