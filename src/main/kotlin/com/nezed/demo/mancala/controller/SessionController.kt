package com.nezed.demo.mancala.controller

import com.nezed.demo.mancala.model.dto.SessionMoveDto
import com.nezed.demo.mancala.model.response.SessionResponse
import com.nezed.demo.mancala.model.response.asSessionResponse
import com.nezed.demo.mancala.service.SessionService
import com.nezed.demo.mancala.service.exception.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/session"], produces = [MediaType.APPLICATION_JSON_VALUE])
class SessionController(private val sessionService: SessionService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new session with a board with a creator as player 'ONE'")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Success")
    )
    fun create(): SessionResponse {
        return sessionService.create().asSessionResponse()
    }

    @PutMapping(path = ["/{sessionId}"])
    @Operation(summary = "Join the session as player 'TWO'")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Successfully joined"),
        ApiResponse(responseCode = "403", description = "Session is full", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Session not found", content = [Content()])
    )
    fun join(
        @PathVariable sessionId: String
    ): SessionResponse? {
        return sessionService.joinSession(sessionId).asSessionResponse()
    }

    @GetMapping(path = ["/{sessionId}"])
    @Operation(summary = "Retrieve specific session")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Success"),
        ApiResponse(responseCode = "403", description = "You are not a member of this session", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Session not found", content = [Content()])
    )
    fun getGameById(@PathVariable sessionId: String): SessionResponse {
        return sessionService.getSessionById(sessionId).asSessionResponse()
    }

    @PutMapping(path = ["/{sessionId}/move"])
    @Operation(summary = "Make a move on session board")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Success"),
        ApiResponse(responseCode = "400", description = "Game on this board is finished or such move is not allowed as it violates game rules", content = [Content()]),
        ApiResponse(responseCode = "403", description = "You are not a member of this session", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Session not found", content = [Content()])
    )
    fun move(
        @PathVariable sessionId: String,
        @Valid @RequestBody moveDto: SessionMoveDto,
    ): SessionResponse? {
        return sessionService.move(sessionId, moveDto).asSessionResponse()
    }
}