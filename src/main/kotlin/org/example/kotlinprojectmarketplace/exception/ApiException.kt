package org.example.kotlinprojectmarketplace.exception

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@JsonIgnoreProperties("cause", "stackTrace", "localizedMessage", "suppressed")
sealed class ApiException(
    override val message: String = "Something went wrong",

    val statusCode: HttpStatus = HttpStatus.BAD_REQUEST,

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss[.SSS]")
    val happenedAt: LocalDateTime = LocalDateTime.now(),

) : RuntimeException()