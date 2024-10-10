package org.example.kotlinprojectmarketplace.controller

import org.example.kotlinprojectmarketplace.exception.ApiException
import org.example.kotlinprojectmarketplace.exception.AuthException
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionController {

    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: AuthException): ResponseEntity<AuthException> =
        ResponseEntity.status(e.statusCode).contentType(MediaType.APPLICATION_JSON).body(e)
}
