package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockitoExtension::class)
class AuthServiceImplTest {
    // создает Mock - мнимый объект, имитурующий работу реального
    @Mock
    private lateinit var userDetailsRepository: UserDetailsRepository

    // создает объект, в работе которого будет использоваться Mock
    @InjectMocks
    private lateinit var authService: AuthServiceImpl

    private val passwordEncoder = BCryptPasswordEncoder()

    @Test
    fun testSuccessRegister() {
        val authRequest = AuthRequest("test", "test")

        `when`(userDetailsRepository
            .findByLogin(authRequest.login))
            .thenReturn(Optional.empty())

        authService.register(authRequest)

        verify(userDetailsRepository).findByLogin(authRequest.login)
    }

    @Test
    fun testRegisterDuplicateLoginException() {
        val authRequest = AuthRequest("test", "test")
        val userDetails = UserDetails(1, authRequest.password, passwordEncoder.encode(authRequest.password))

        `when`(userDetailsRepository
            .findByLogin(authRequest.login))
            .thenReturn(Optional.of(userDetails))

        assertThrows(AuthException::class.java) { authService.register(authRequest) }

        verify(userDetailsRepository).findByLogin(authRequest.login)

    }

    @Test
    fun testSuccessLogin() {
        val authRequest = AuthRequest("test", "test")
        val userDetails = UserDetails(1, authRequest.login, passwordEncoder.encode(authRequest.password))

        `when`(userDetailsRepository
            .findByLogin(authRequest.login))
            .thenReturn(Optional.of(userDetails))

        authService.login(authRequest)

        verify(userDetailsRepository).findByLogin(authRequest.login)
    }

    @Test
    fun testLoginWrongCredentialsException() {
        val wrongLoginAuthRequest = AuthRequest("wrong", "test")
        val wrongPasswordAuthRequest = AuthRequest("test", "wrong")
        val userDetails = UserDetails(1, "test", passwordEncoder.encode("test"))

        `when`(userDetailsRepository
            .findByLogin(wrongLoginAuthRequest.login))
            .thenReturn(Optional.empty())

        `when`(userDetailsRepository
            .findByLogin(wrongPasswordAuthRequest.login))
            .thenReturn(Optional.of(userDetails))

        assertThrows(AuthException::class.java) { authService.login(wrongLoginAuthRequest) }

        assertThrows(AuthException::class.java) { authService.login(wrongPasswordAuthRequest) }

        verify(userDetailsRepository).findByLogin(wrongLoginAuthRequest.login)
        verify(userDetailsRepository).findByLogin(wrongPasswordAuthRequest.login)
    }
}