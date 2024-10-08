package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthException
import org.example.kotlinprojectmarketplace.exception.AuthExceptionMessage
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

    override fun register(authRequest: AuthRequest) {
        if (userDetailsRepository.findByLogin(authRequest.login).isPresent) {
            throw AuthException(AuthExceptionMessage.DUPLICATED_LOGIN.text)
        }

        val userDetails = UserDetails(
            login = authRequest.login,
            password = passwordEncoder.encode(authRequest.password)
        )
        userDetailsRepository.save(userDetails)
    }

    override fun login(authRequest: AuthRequest) {
        val userDetails = with(userDetailsRepository.findByLogin(authRequest.login)) {
            if (this.isEmpty) throw AuthException(AuthExceptionMessage.WRONG_CREDENTIALS.text)
            this.get()
        }
        if (!passwordEncoder.matches(authRequest.password, userDetails.password)) {
            throw AuthException(AuthExceptionMessage.WRONG_CREDENTIALS.text)
        }
    }
}