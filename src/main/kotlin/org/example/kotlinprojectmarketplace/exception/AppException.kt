package org.example.kotlinprojectmarketplace.exception

sealed class AppException(
    override val message: String = "Something went wrong"
) : RuntimeException()