package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.config.JwtProperties
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthException
import org.example.kotlinprojectmarketplace.exception.AuthExceptionMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl(
    @Autowired
    private val userDetailsRepository: UserDetailsRepository,
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder,
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val jwtProperties: JwtProperties,
) : AuthService {
    override fun register(
        authRequest: AuthRequest
    ): AuthResponse {
        if (userDetailsRepository.findByLogin(authRequest.login).isPresent) {
            throw AuthException(AuthExceptionMessage.DUPLICATED_LOGIN, HttpStatus.CONFLICT)
        }

        val userDetails = UserDetails(
            login = authRequest.login,
            password = passwordEncoder.encode(authRequest.password)
        )
        val savedUserDetails = userDetailsRepository.save(userDetails)
        return AuthResponse(
            jwtService.generateToken(savedUserDetails, Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)),
            //todo тут надо со временем уточнить моментики
            AuthResponseMessage.SUCCESS_REGISTRATION
        )
    }

    override fun login(
        authRequest: AuthRequest
    ): AuthResponse {
        val userDetails = userDetailsRepository.findByLogin(authRequest.login)
            .orElseThrow { AuthException(AuthExceptionMessage.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED) }

        if (!passwordEncoder.matches(authRequest.password, userDetails.password)) {
            throw AuthException(AuthExceptionMessage.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED)
        }

        return AuthResponse(
            jwtService.generateToken(userDetails, Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)),
            AuthResponseMessage.SUCCESS_LOGIN
        )
    }
}