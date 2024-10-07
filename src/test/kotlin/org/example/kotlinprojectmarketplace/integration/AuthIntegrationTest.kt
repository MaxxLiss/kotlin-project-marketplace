package org.example.kotlinprojectmarketplace.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthRequest
import org.example.kotlinprojectmarketplace.database.dto.auth.AuthResponseMessage
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.exception.auth.DuplicateLoginException
import org.example.kotlinprojectmarketplace.exception.auth.WrongCredentialsException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AuthIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @SpyBean
    private lateinit var userDetailsRepository: UserDetailsRepository

    private val objectMapper = ObjectMapper()

    private val passwordEncoder = BCryptPasswordEncoder()

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
            .andExpect(status().isOk)
            .andExpect(content().string(AuthResponseMessage.SUCCESS_REGISTRATION.text))

        verify(userDetailsRepository).findByLogin(authRequest.login)

        val userDetails = UserDetails(
            login = authRequest.login,
            password = authRequest.password
        )
        // todo проверить, что вызывается save()
        // возможно, получится сделать save(Any), но кажется, что можно лучше
//        verify(userDetailsRepository).save(userDetails)

        val userDetailsResult = userDetailsRepository.findByLogin(authRequest.login)

        assertNotNull(userDetailsResult)

        assertEquals(userDetails.login, userDetailsResult?.login)
        assert(passwordEncoder.matches(userDetails.password, userDetailsResult?.password))
    }

    @Test
    fun testDuplicateLoginExRegister() {
        val authRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(AuthResponseMessage.SUCCESS_REGISTRATION.text))

        verify(userDetailsRepository).findByLogin(authRequest.login)

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(DuplicateLoginException().message))

        val userDetails = UserDetails(
            login = authRequest.login,
            password = authRequest.password
        )

        val userDetailsResult = userDetailsRepository.findByLogin(authRequest.login)

        assertNotNull(userDetailsResult)

        assertEquals(userDetails.login, userDetailsResult?.login)
        assert(passwordEncoder.matches(userDetails.password, userDetailsResult?.password))
    }

    @Test
    fun testSuccessLogin() {
        val authRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(AuthResponseMessage.SUCCESS_REGISTRATION.text))

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(AuthResponseMessage.SUCCESS_LOGIN.text))

        verify(userDetailsRepository, times(2)).findByLogin(authRequest.login)

        val userDetails = UserDetails(
            login = authRequest.login,
            password = authRequest.password
        )

        val userDetailsResult = userDetailsRepository.findByLogin(authRequest.login)

        assertNotNull(userDetailsResult)

        assertEquals(userDetails.login, userDetailsResult?.login)
        assert(passwordEncoder.matches(userDetails.password, userDetailsResult?.password))
    }

    @Test
    fun testWrongCredentialsExLogin() {
        val successAuthRequest = AuthRequest("test", "test")

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(successAuthRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().string(AuthResponseMessage.SUCCESS_REGISTRATION.text))

        val wrongLoginAuthRequest = AuthRequest("wrong", "test")
        val wrongPasswordAuthRequest = AuthRequest("test", "wrong")

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongLoginAuthRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(WrongCredentialsException().message))

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongPasswordAuthRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(WrongCredentialsException().message))

        verify(userDetailsRepository, times(2)).findByLogin(successAuthRequest.login)
        verify(userDetailsRepository).findByLogin(wrongLoginAuthRequest.login)

        val userDetails = UserDetails(
            login = successAuthRequest.login,
            password = successAuthRequest.password
        )

        val userDetailsResult = userDetailsRepository.findByLogin(successAuthRequest.login)

        assertNotNull(userDetailsResult)

        assertEquals(userDetails.login, userDetailsResult?.login)
        assert(passwordEncoder.matches(userDetails.password, userDetailsResult?.password))
    }
}