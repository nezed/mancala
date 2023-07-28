package com.nezed.demo.mancala.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nezed.demo.mancala.model.response.SessionResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerGetTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun whenValidMove_shouldReturnOk() {
        val creationResult = mockMvc.post("/api/session").andReturn()
        val createSessionResponse = objectMapper.readValue(
            creationResult.response.contentAsString,
            SessionResponse::class.java,
        )
        val joinResult = mockMvc.put("/api/session/${createSessionResponse.id}").andReturn()

        mockMvc.get("/api/session/${createSessionResponse.id}") {
            creationResult.response.cookies.forEach { cookie(it) }
        }.andExpect {
            status { isOk() }
            jsonPath("$.board.state") {
                value("NEW")
            }
        }

        mockMvc.get("/api/session/${createSessionResponse.id}") {
            joinResult.response.cookies.forEach { cookie(it) }
        }.andExpect {
            status { isOk() }
            jsonPath("$.board.state") {
                value("NEW")
            }
        }
    }
}