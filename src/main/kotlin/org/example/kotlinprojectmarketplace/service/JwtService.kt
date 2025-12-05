package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import java.util.*

interface JwtService {
    fun generateToken(
        userDetails: UserDetails,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String

    fun isValid(token: String, userDetails: UserDetails): Boolean

    fun extractId(token: String): Optional<Int>
}