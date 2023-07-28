package com.nezed.demo.mancala.integration

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post


@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerCreateTest {
    @Autowired lateinit var mockMvc: MockMvc

    @Test
    fun whenCreated_shouldReturnCreated() {
        mockMvc.post("/api/session") {
            content = ""
        }.andExpect {
            status { isCreated() }
            jsonPath("$.board.state") {
                value("NEW")
            }
        }
    }
}