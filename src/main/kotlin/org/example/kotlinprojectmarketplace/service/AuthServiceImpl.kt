package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthException
import org.example.kotlinprojectmarketplace.exception.AuthExceptionMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    @Autowired
    private val userDetailsRepository: UserDetailsRepository,

    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder
) : AuthService {
    override fun register(authRequest: AuthRequest) {
        if (userDetailsRepository.findByLogin(authRequest.login).isPresent) {
            throw AuthException(AuthExceptionMessage.DUPLICATED_LOGIN, HttpStatus.CONFLICT)
        }

        val userDetails = UserDetails(
            login = authRequest.login,
            password = passwordEncoder.encode(authRequest.password)
        )
        userDetailsRepository.save(userDetails)
    }

    override fun login(authRequest: AuthRequest) {
        val userDetails = userDetailsRepository.findByLogin(authRequest.login).let {
            if (it.isEmpty) throw AuthException(AuthExceptionMessage.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED)
            it.get()
        }
        if (!passwordEncoder.matches(authRequest.password, userDetails.password)) {
            throw AuthException(AuthExceptionMessage.INVALID_CREDENTIALS, HttpStatus.UNAUTHORIZED)
        }
    }
}