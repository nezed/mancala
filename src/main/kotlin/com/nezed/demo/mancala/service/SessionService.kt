package com.nezed.demo.mancala.service

import com.nezed.demo.mancala.model.Session
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.dto.SessionMoveDto
import com.nezed.demo.mancala.repository.SessionRepository
import com.nezed.demo.mancala.service.exception.*
import jakarta.servlet.http.HttpSession
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val boardService: BoardService,
    private val httpSession: HttpSession,
) {
    fun create(): Session {
        return sessionRepository.save(Session(playerOneSessionId = httpSession.id))
    }

    @Throws(SessionFullException::class, SessionNotFoundException::class)
    fun joinSession(sessionId: String): Session {
        val session = findById(sessionId)
        if (!session.isJoinable()) {
            throw SessionFullException()
        }
        session.join(httpSession.id)
        return sessionRepository.save(session)
    }

    @Throws(SessionAccessException::class, SessionNotFoundException::class)
    fun getSessionById(sessionId: String): Session {
        val session = findById(sessionId)

        session.joinedAs(httpSession.id) ?: throw SessionAccessException()

        return session
    }

    @Throws(SessionAccessException::class, SessionNotFoundException::class)
    fun move(id: String, moveDto: SessionMoveDto): Session {
        val session = getSessionById(id)
        val movePlayer = session.joinedAs(httpSession.id)
        // This is just a type-guard
        movePlayer ?: throw SessionAccessException()

        boardService.move(session.board, movePlayer, moveDto.pitIndex)
        return sessionRepository.save(session)
    }

    private fun findById(sessionId: String): Session {
        return sessionRepository.findById(ObjectId(sessionId)).orElseThrow { SessionNotFoundException() }
    }
}