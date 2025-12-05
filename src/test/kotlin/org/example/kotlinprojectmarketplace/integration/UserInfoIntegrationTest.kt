package org.example.kotlinprojectmarketplace.integration

import org.example.kotlinprojectmarketplace.database.dto.*
import org.example.kotlinprojectmarketplace.database.entity.UserDetails
import org.example.kotlinprojectmarketplace.database.entity.UserInfo
import org.example.kotlinprojectmarketplace.database.repository.UserDetailsRepository
import org.example.kotlinprojectmarketplace.database.repository.UserInfoRepository
import org.example.kotlinprojectmarketplace.service.JwtService
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import kotlin.test.assertEquals

class UserInfoIntegrationTest : AbstractIntegrationTest() {
    @Autowired
    lateinit var userDetailsRepository: UserDetailsRepository

    @SpyBean
    lateinit var userInfoRepository: UserInfoRepository

    @SpyBean
    lateinit var jwtService: JwtService

    private val registeredAuthRequest: AuthRequest = AuthRequest("test", "test")
    private lateinit var registeredJwtToken: String

    @BeforeEach
    fun registerUserDetails() {
        val userDetails = userDetailsRepository.save(
            UserDetails(
                registeredAuthRequest.login,
                passwordEncoder.encode(registeredAuthRequest.password)
            )
        )

        assert(userDetailsRepository.existsByLogin(registeredAuthRequest.login))

        registeredJwtToken = jwtService.generateToken(userDetails)
        assertEquals(userDetails.id, jwtService.extractId(registeredJwtToken).run {
            assert(isPresent)
            get()
        })

        reset(jwtService)
    }

    fun createUserInfo(id: Int): UserInfo {
        val userInfo = UserInfo(id, "test", "test", "$id@test.com", "+${id}test", 10)
        userInfoRepository.save(userInfo)
        userDetailsRepository.findById(id).run {
            assert(isPresent)
        }

        reset(jwtService)
        reset(userInfoRepository)
        return userInfo
    }

    @Test
    fun successCreateUserInfoTest() {
        val id = jwtService.extractId(registeredJwtToken).get()

        val userInfoEnrichRequest = UserInfoEnrichRequest("test", "test",
            "test", "test", 100)
        assert(!userInfoRepository.existsById(id))

        mockMvc.perform(
            post("/api/user/enrich")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoEnrichRequest))
                .header("Authorization", "Bearer $registeredJwtToken")
        )
            .andExpect {
                status().isCreated
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_CREATE))
                jsonPath("$.userInfo.name", equalTo(userInfoEnrichRequest.name))
                jsonPath("$.userInfo.lastName", equalTo(userInfoEnrichRequest.lastName))
                jsonPath("$.userInfo.email", equalTo(userInfoEnrichRequest.email))
                jsonPath("$.userInfo.phone", equalTo(userInfoEnrichRequest.phone))
                jsonPath("$.userInfo.cash", equalTo(userInfoEnrichRequest.cash))
            }

        verify(userInfoRepository).findById(id)
        verify(userInfoRepository).save(any())
        verify(userInfoRepository).findByEmail(any())
        verify(userInfoRepository).findByPhone(any())

        assert(userInfoRepository.existsById(id))
        val userInfo = userInfoRepository.findById(id).get()
        assertEquals(id, userInfo.id)
        assertEquals(userInfoEnrichRequest.name, userInfo.name)
        assertEquals(userInfoEnrichRequest.lastName, userInfo.lastName)
        assertEquals(userInfoEnrichRequest.phone, userInfo.phone)
        assertEquals(userInfoEnrichRequest.email, userInfo.email)
        assertEquals(userInfoEnrichRequest.cash, userInfo.cash)
    }

    @Test
    fun successUpdateUserInfoTest() {
        val id = jwtService.extractId(registeredJwtToken).get()
        val registeredUserInfo = createUserInfo(id)

        val userInfoEnrichRequest = UserInfoEnrichRequest(
            "not${registeredUserInfo.name}",
            "not${registeredUserInfo.lastName}",
            "not${registeredUserInfo.email}",
            "not${registeredUserInfo.phone}",
            registeredUserInfo.cash + 10)

        mockMvc.perform(
            put("/api/user/enrich")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInfoEnrichRequest))
                .header("Authorization", "Bearer $registeredJwtToken")
        )
            .andExpect {
                status().isOk
                jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_UPDATE))
                jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_CREATE))
                jsonPath("$.userInfo.name", equalTo(userInfoEnrichRequest.name))
                jsonPath("$.userInfo.lastName", equalTo(userInfoEnrichRequest.lastName))
                jsonPath("$.userInfo.email", equalTo(userInfoEnrichRequest.email))
                jsonPath("$.userInfo.phone", equalTo(userInfoEnrichRequest.phone))
                jsonPath("$.userInfo.cash", equalTo(userInfoEnrichRequest.cash))
            }

        verify(userInfoRepository).findById(id)
        verify(userInfoRepository).save(any())
        verify(userInfoRepository).findByEmail(any())
        verify(userInfoRepository).findByPhone(any())

        assert(userInfoRepository.existsById(id))
        val userInfo = userInfoRepository.findById(id).get()
        assertEquals(id, userInfo.id)
        assertEquals(userInfoEnrichRequest.name, userInfo.name)
        assertEquals(userInfoEnrichRequest.lastName, userInfo.lastName)
        assertEquals(userInfoEnrichRequest.phone, userInfo.phone)
        assertEquals(userInfoEnrichRequest.email, userInfo.email)
        assertEquals(userInfoEnrichRequest.cash, userInfo.cash)
    }

    @Test
    fun successDeleteUserInfoTest() {
        val id = jwtService.extractId(registeredJwtToken).get()
        val registeredUserInfo = createUserInfo(id)

        mockMvc.perform(
            delete("/api/user/enrich")
                .header("Authorization", "Bearer $registeredJwtToken")
        )
            .andExpect {
                status().isOk
                jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_UPDATE))
                jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_CREATE))
                jsonPath("$.userInfo.name", equalTo(registeredUserInfo.name))
                jsonPath("$.userInfo.lastName", equalTo(registeredUserInfo.lastName))
                jsonPath("$.userInfo.email", equalTo(registeredUserInfo.email))
                jsonPath("$.userInfo.phone", equalTo(registeredUserInfo.phone))
                jsonPath("$.userInfo.cash", equalTo(registeredUserInfo.cash))
            }

        verify(userInfoRepository).findById(id)
        verify(userInfoRepository, never()).save(any())
        verify(userInfoRepository, never()).findByEmail(any())
        verify(userInfoRepository, never()).findByPhone(any())

        assert(!userInfoRepository.existsById(id))
    }

    @Test
    fun successGetUserInfoTest() {
        val id = jwtService.extractId(registeredJwtToken).get()
        val registeredUserInfo = createUserInfo(id)

        mockMvc.perform(
            get("/api/user/enrich")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registeredUserInfo))
                .header("Authorization", "Bearer $registeredJwtToken")
        )
        .andExpect {
            status().isOk
            jsonPath("$.message", equalTo(UserInfoEnrichResponseMessage.SUCCESS_GET))
            jsonPath("$.userInfo.name", equalTo(registeredUserInfo.name))
            jsonPath("$.userInfo.lastName", equalTo(registeredUserInfo.lastName))
            jsonPath("$.userInfo.email", equalTo(registeredUserInfo.email))
            jsonPath("$.userInfo.phone", equalTo(registeredUserInfo.phone))
            jsonPath("$.userInfo.cash", equalTo(registeredUserInfo.cash))
        }

        verify(userInfoRepository).findById(id)
        verify(userInfoRepository, never()).save(any())
        verify(userInfoRepository, never()).findByEmail(any())
        verify(userInfoRepository, never()).findByPhone(any())
    }
}