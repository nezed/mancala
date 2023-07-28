package com.nezed.demo.mancala.unit.service

import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.model.dto.SessionMoveDto
import com.nezed.demo.mancala.repository.SessionRepository
import com.nezed.demo.mancala.service.AssertService
import com.nezed.demo.mancala.service.BoardService
import com.nezed.demo.mancala.service.BoardValidationService
import com.nezed.demo.mancala.service.SessionService
import com.nezed.demo.mancala.service.exception.SessionAccessException
import com.nezed.demo.mancala.service.exception.SessionFullException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import jakarta.servlet.http.HttpSession
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class SessionServiceUnitTests {
    @MockK
    lateinit var sessionRepository: SessionRepository

    @MockK
    lateinit var httpSession: HttpSession

    @SpyK
    var assertService = AssertService()

    @SpyK
    var boardValidationService = BoardValidationService(assertService)

    @SpyK
    var boardService = BoardService(boardValidationService)

    @InjectMockKs
    lateinit var sessionService: SessionService

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @BeforeEach
    fun beforeEach() {
        every { httpSession.id } returns "player-session-one"
        every { sessionRepository.save(any()) } returnsArgument(0)
    }

    @Test
    fun whenCreateSession_thenReturnNewJoinedSession() {
        val session = sessionService.create()

        assertEquals(session.joinedAs(httpSession.id), BoardPlayerEnum.ONE)
    }

    @Test
    fun whenJoinNewSession_thenReturnAJoinedSession() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-two"

        sessionService.joinSession(session.id.toHexString())

        assertEquals(session.joinedAs("player-session-one"), BoardPlayerEnum.ONE)
        assertEquals(session.joinedAs(httpSession.id), BoardPlayerEnum.TWO)
    }

    @Test
    fun whenCantJoinFullSession_thenThrows() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-two"
        sessionService.joinSession(session.id.toHexString())
        every { httpSession.id } returns "player-session-three"

        assertThrows<SessionFullException> {
            sessionService.joinSession(session.id.toHexString())
        }
    }

    @Test
    fun whenPlayerOneRetrievesTheSession_thenReturnSession() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)

        val moveSession = sessionService.getSessionById(session.id.toHexString())

        assertEquals(moveSession, session)
    }

    @Test
    fun whenPlayerTwoRetrievesTheSession_thenReturnSession() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-two"
        sessionService.joinSession(session.id.toHexString())

        val moveSession = sessionService.getSessionById(session.id.toHexString())

        assertEquals(moveSession, session)
    }

    @Test
    fun whenUnknownPlayerRetrievesTheSession_thenThrows() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-three"

        assertThrows<SessionAccessException> {
            sessionService.getSessionById(session.id.toHexString())
        }
    }

    @Test
    fun whenPlayerOneMoveOnSession_thenReturnSessionWithBoardInProgress() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        session.board.next = BoardPlayerEnum.ONE
        every { sessionRepository.findById(any()) } returns Optional.of(session)

        val moveSession = sessionService.move(session.id.toHexString(), SessionMoveDto(0))

        assertEquals(moveSession.board.state, BoardStateEnum.IN_PROGRESS)
    }

    @Test
    fun whenPlayerTwoMoveOnSession_thenReturnSessionWithBoardInProgress() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        session.board.next = BoardPlayerEnum.TWO
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-two"
        sessionService.joinSession(session.id.toHexString())

        val moveSession = sessionService.move(session.id.toHexString(), SessionMoveDto(0))

        assertEquals(moveSession.board.state, BoardStateEnum.IN_PROGRESS)
    }

    @Test
    fun whenUnknownPlayerMoveOnSession_thenThrows() {
        every { sessionRepository.save(any()) } returnsArgument(0)
        val session = sessionService.create()
        every { sessionRepository.findById(any()) } returns Optional.of(session)
        every { httpSession.id } returns "player-session-three"

        assertThrows<SessionAccessException> {
            sessionService.move(session.id.toHexString(), SessionMoveDto(0))
        }
    }
}