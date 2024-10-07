package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.auth.AuthException
import org.example.kotlinprojectmarketplace.exception.auth.DuplicateLoginException
import org.example.kotlinprojectmarketplace.exception.auth.WrongCredentialsException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class AuthServiceImpl(
    @Autowired
    private val userDetailsRepository: UserDetailsRepository
) : AuthService {

    private val passwordEncoder = BCryptPasswordEncoder()

    @Throws(AuthException::class)
    override fun register(authRequest: AuthRequest): AuthResponse {
        if (userDetailsRepository.findByLogin(authRequest.login) != null) {
            throw DuplicateLoginException()
        }

        val userDetails = UserDetails(
            login = authRequest.login,
            password = passwordEncoder.encode(authRequest.password)
        )
        userDetailsRepository.save(userDetails)

        return AuthResponse(AuthResponseMessage.SUCCESS_REGISTRATION)
    }

    @Throws(AuthException::class)
    override fun login(authRequest: AuthRequest): AuthResponse {
        val userDetails = userDetailsRepository.findByLogin(authRequest.login) ?: throw WrongCredentialsException()
        if (!passwordEncoder.matches(authRequest.password, userDetails.password)) {
            throw WrongCredentialsException()
        }

        return AuthResponse(AuthResponseMessage.SUCCESS_LOGIN)
    }
}