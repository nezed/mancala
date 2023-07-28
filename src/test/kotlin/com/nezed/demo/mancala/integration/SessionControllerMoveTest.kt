package com.nezed.demo.mancala.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.dto.SessionMoveDto
import com.nezed.demo.mancala.model.response.SessionResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put


@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SessionControllerMoveTest {
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

        mockMvc.put("/api/session/${createSessionResponse.id}/move") {
            when(createSessionResponse.board.nextPlayer) {
                // MockHttpSession doesn't actually change or preserve httpSession.id
                BoardPlayerEnum.ONE -> creationResult.response.cookies.forEach { cookie(it) }
                BoardPlayerEnum.TWO -> joinResult.response.cookies.forEach { cookie(it) }
            }
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(
                SessionMoveDto(0)
            )
        }.andExpect {
            status { isOk() }
            jsonPath("$.board.state") {
                value("IN_PROGRESS")
            }
        }
    }
}