package org.example.kotlinprojectmarketplace.service

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.auth.DuplicateLoginException
import org.example.kotlinprojectmarketplace.exception.auth.WrongCredentialsException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

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
            .findByLogin("test"))
            .thenReturn(null)

        val result = authService.register(authRequest)

        assertEquals(result, AuthResponse(AuthResponseMessage.SUCCESS_REGISTRATION))
    }

    @Test
    fun testRegisterDuplicateLoginException() {
        val authRequest = AuthRequest("test", "test")
        val userDetails = UserDetails(1, "test", passwordEncoder.encode("test"))

        `when`(userDetailsRepository
            .findByLogin("test"))
            .thenReturn(userDetails)

        assertThrows(DuplicateLoginException::class.java) { authService.register(authRequest) }
    }

    @Test
    fun testSuccessLogin() {
        val authRequest = AuthRequest("test", "test")
        val userDetails = UserDetails(1, "test", passwordEncoder.encode("test"))

        `when`(userDetailsRepository
            .findByLogin("test"))
            .thenReturn(userDetails)

        val result = authService.login(authRequest)

        assertEquals(result, AuthResponse(AuthResponseMessage.SUCCESS_LOGIN))
    }

    @Test
    fun testLoginWrongCredentialsException() {
        val wrongLoginAuthRequest = AuthRequest("wrong", "test")
        val wrongPasswordAuthRequest = AuthRequest("test", "wrong")
        val userDetails = UserDetails(1, "test", passwordEncoder.encode("test"))

        `when`(userDetailsRepository
            .findByLogin("wrong"))
            .thenReturn(null)

        `when`(userDetailsRepository
            .findByLogin("test"))
            .thenReturn(userDetails)

        assertThrows(WrongCredentialsException::class.java) { authService.login(wrongLoginAuthRequest) }

        assertThrows(WrongCredentialsException::class.java) { authService.login(wrongPasswordAuthRequest) }
    }
}