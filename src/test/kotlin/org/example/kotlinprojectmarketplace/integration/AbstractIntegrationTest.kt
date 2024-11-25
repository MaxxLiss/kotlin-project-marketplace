package org.example.kotlinprojectmarketplace.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
abstract class AbstractIntegrationTest {
    @Autowired
    protected final lateinit var mockMvc: MockMvc

    @Autowired
    protected final lateinit var objectMapper: ObjectMapper

    @Autowired
    protected final lateinit var passwordEncoder: BCryptPasswordEncoder
}