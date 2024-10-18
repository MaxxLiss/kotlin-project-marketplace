package org.example.kotlinprojectmarketplace.integration

import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.AuthExceptionMessage
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class AuthIntegrationTest : AbstractIntegrationTest() {
    @SpyBean
    private lateinit var userDetailsRepository: UserDetailsRepository

    @Test
    fun testSuccessRegister() {
        // todo тесты, которые не упадут, если такой юзер занят
        // как вариант, можно или сгенерировать юзера, который точно не занят или занять имя test
        // оба варианта кажутся не очень
        val authRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_REGISTRATION)))

        verify(userDetailsRepository).findByLogin(authRequest.login)
        verify(userDetailsRepository).save(any(UserDetails::class.java))

        val userDetailsResult = userDetailsRepository.findByLogin(authRequest.login).let {
            assert(it.isPresent)
            it.get()
        }

        assertEquals(authRequest.login, userDetailsResult.login)
        assert(passwordEncoder.matches(authRequest.password, userDetailsResult.password))
    }

    @Test
    fun testDuplicateLoginExRegister() {
        val authRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_REGISTRATION)))

        assert(userDetailsRepository.findByLogin(authRequest.login).isPresent)

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isConflict)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthExceptionMessage.DUPLICATED_LOGIN)))

        verify(userDetailsRepository, times(3)).findByLogin(authRequest.login)
        verify(userDetailsRepository).save(any(UserDetails::class.java))
    }

    @Test
    fun testSuccessLogin() {
        val authRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_REGISTRATION)))

        verify(userDetailsRepository).save(any(UserDetails::class.java))
        assert(userDetailsRepository.findByLogin(authRequest.login).isPresent)

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_LOGIN)))

        verify(userDetailsRepository, times(3)).findByLogin(authRequest.login)

        val userDetailsResult = userDetailsRepository.findByLogin(authRequest.login).let {
            assert(it.isPresent)
            it.get()
        }

        assertEquals(authRequest.login, userDetailsResult.login)
        assert(passwordEncoder.matches(authRequest.password, userDetailsResult.password))
    }

    @Test
    fun testInvalidCredentialsExLogin() {
        val successAuthRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(successAuthRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthResponseMessage.SUCCESS_REGISTRATION)))

        verify(userDetailsRepository).save(any(UserDetails::class.java))
        assert(userDetailsRepository.findByLogin(successAuthRequest.login).isPresent)

        val wrongLoginAuthRequest = AuthRequest("wrong", "test")
        val wrongPasswordAuthRequest = AuthRequest("test", "wrong")

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongLoginAuthRequest))
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthExceptionMessage.INVALID_CREDENTIALS)))

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPasswordAuthRequest))
        )
            .andExpect(status().isUnauthorized)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message", equalTo(AuthExceptionMessage.INVALID_CREDENTIALS)))

        verify(userDetailsRepository, times(3)).findByLogin(successAuthRequest.login)
        verify(userDetailsRepository).findByLogin(wrongLoginAuthRequest.login)

        val userDetailsResult = userDetailsRepository.findByLogin(successAuthRequest.login).let {
            assert(it.isPresent)
            it.get()
        }

        assertEquals(successAuthRequest.login, userDetailsResult.login)
        assert(passwordEncoder.matches(successAuthRequest.password, userDetailsResult.password))
    }
}