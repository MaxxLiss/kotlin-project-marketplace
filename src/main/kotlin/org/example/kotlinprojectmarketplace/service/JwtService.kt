package org.example.kotlinprojectmarketplace.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.example.kotlinprojectmarketplace.config.JwtProperties
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Autowired
    private val jwtProperties: JwtProperties
) {
    private val secretKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    fun generateToken(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ): String = Jwts.builder()
        .claims()
        .subject(userDetails.id.toString())
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(expirationDate)
        .add(additionalClaims)
        .and()
        .signWith(secretKey)
        .compact()

    fun isValid(
        token: String,
        userDetails: UserDetails
    ): Boolean = extractId(token).let {
        it.isPresent && it.get() == userDetails.id && !isExpired(token)
    }

    fun extractId(token: String): Optional<Int> = Optional.of(getAllClaims(token).subject.toInt())

    fun isExpired(
        token: String
    ): Boolean = getAllClaims(token)
        .expiration
        .before(Date(System.currentTimeMillis()))

    private fun getAllClaims(
        token: String
    ): Claims = Jwts
        .parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .payload
}