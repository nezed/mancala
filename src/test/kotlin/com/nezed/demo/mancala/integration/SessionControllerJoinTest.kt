package com.nezed.demo.mancala.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nezed.demo.mancala.model.response.SessionResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpSession
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerJoinTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun whenJoined_shouldReturnOk() {
        val httpSessionOne = MockHttpSession()
        val httpSessionTwo = MockHttpSession()

        val result = mockMvc.post("/api/session") {
            session = httpSessionOne
        }.andReturn()
        val response = objectMapper.readValue(result.response.contentAsString, SessionResponse::class.java)

        mockMvc.put("/api/session/${response.id}") {
            session = httpSessionTwo
        }.andExpect {
            status { isOk() }
            jsonPath("$.board.state") {
                value("NEW")
            }
        }
    }
}