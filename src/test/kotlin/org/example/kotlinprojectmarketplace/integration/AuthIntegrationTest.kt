package org.example.kotlinprojectmarketplace.integration

import org.example.kotlinprojectmarketplace.database.dto.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.AuthResponse
import org.example.kotlinprojectmarketplace.database.dto.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthExceptionMessage
import org.example.kotlinprojectmarketplace.service.JwtService
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

// todo тесты, которые не упадут, если такой юзер занят
class AuthIntegrationTest : AbstractIntegrationTest() {
    @SpyBean
    private lateinit var userDetailsRepository: UserDetailsRepository

    @SpyBean
    private lateinit var jwtService: JwtService

    private val registeredAuthRequest: AuthRequest = AuthRequest("test", "test")

    @BeforeEach
    fun registerUserDetails() {
        userDetailsRepository.save(
            UserDetails(
                registeredAuthRequest.login,
                passwordEncoder.encode(registeredAuthRequest.password)
            )
        )
        assert(userDetailsRepository.existsByLogin(registeredAuthRequest.login))
        reset(userDetailsRepository)
    }

    @Test
    fun testSuccessRegister() {
        val authRequest = AuthRequest("not${registeredAuthRequest.login}", "not${registeredAuthRequest.password}")

        val jwtToken = mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect {
                status().isCreated
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_REGISTRATION))
            }
            .andReturn().response.contentAsString.let {
                objectMapper.readValue(it, AuthResponse::class.java).jwtToken
            }

        verify(userDetailsRepository).findByLogin(authRequest.login)
        verify(userDetailsRepository).save(any())
        verify(jwtService).generateToken(any(), any())

        val id = jwtService.extractId(jwtToken).run {
            assert(isPresent)
            get()
        }
        val userDetails = userDetailsRepository.findByLogin(authRequest.login).run {
            assert(isPresent)
            get()
        }

        assertEquals(userDetails.id, id)
        assertEquals(authRequest.login, userDetails.login)
        assert(passwordEncoder.matches(authRequest.password, userDetails.password))
    }

    @Test
    fun testDuplicateLoginExRegister() {
        val authRequest = AuthRequest(registeredAuthRequest.login, registeredAuthRequest.password)

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect {
                status().isConflict
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(AuthExceptionMessage.DUPLICATED_LOGIN))
            }

        verify(userDetailsRepository).findByLogin(registeredAuthRequest.login)
        verify(userDetailsRepository, never()).save(any())
        verify(jwtService, never()).generateToken(any(), any())
    }

    @Test
    fun testSuccessLogin() {
        val authRequest = AuthRequest(registeredAuthRequest.login, registeredAuthRequest.password)

        val jwtToken = mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_LOGIN))
            }
            .andReturn().response.contentAsString.let {
                objectMapper.readValue(it, AuthResponse::class.java).jwtToken
            }

        verify(userDetailsRepository).findByLogin(registeredAuthRequest.login)
        verify(userDetailsRepository, never()).save(any())
        verify(jwtService).generateToken(any(), any())

        val id = jwtService.extractId(jwtToken).run {
            assert(isPresent)
            get()
        }
        val acceptedId = userDetailsRepository.findByLogin(registeredAuthRequest.login).run {
            assert(isPresent)
            get().id
        }

        assertEquals(acceptedId, id)
    }

    @Test
    fun testInvalidCredentialsExLogin() {
        val wrongLoginAuthRequest = AuthRequest("not${registeredAuthRequest.login}", "test")
        val wrongPasswordAuthRequest = AuthRequest("test", "not${registeredAuthRequest.password}")

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongLoginAuthRequest))
        )
            .andExpect {
                status().isUnauthorized
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(AuthExceptionMessage.INVALID_CREDENTIALS))
            }

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPasswordAuthRequest))
        )
            .andExpect {
                status().isUnauthorized
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(AuthExceptionMessage.INVALID_CREDENTIALS))
            }

        verify(userDetailsRepository).findByLogin(wrongPasswordAuthRequest.login)
        verify(userDetailsRepository).findByLogin(wrongLoginAuthRequest.login)
        verify(userDetailsRepository, never()).save(any())
        verify(jwtService, never()).generateToken(any(), any())
    }
}