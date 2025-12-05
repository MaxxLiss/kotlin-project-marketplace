package org.example.kotlinprojectmarketplace.database.dto

data class UserInfoEnrichRequest(
    val name: String,
    val lastName: String,
    val email: String? = null,
    val phone: String? = null,
    val cash: Int = 0,
)