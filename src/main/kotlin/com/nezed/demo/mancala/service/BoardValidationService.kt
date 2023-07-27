package com.nezed.demo.mancala.service

import com.nezed.demo.mancala.model.Board
import com.nezed.demo.mancala.model.Session
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.model.dto.SessionMoveDto
import com.nezed.demo.mancala.model.getPlayerPits
import com.nezed.demo.mancala.model.isEmptyPit
import com.nezed.demo.mancala.service.exception.BoardFinishedException
import com.nezed.demo.mancala.service.exception.RulesViolationException

class BoardValidationService(
    private val assert: AssertService,
) {
    @Throws(
        BoardFinishedException::class,
        RulesViolationException::class,
    )
    fun validateMove(board: Board, movePlayer: BoardPlayerEnum, movePit: Int) {
        assert.isTrue(!isGameFinished(board)) {
            BoardFinishedException()
        }

        assert.isTrue(isPlayerNext(board, movePlayer)) {
            RulesViolationException()
        }

        assert.isTrue(isPitHaveStones(board, movePlayer, movePit)) {
            RulesViolationException()
        }
    }

    fun isGameFinished(board: Board): Boolean {
        return board.state === BoardStateEnum.FINISHED
    }

    fun isPlayerNext(board: Board, player: BoardPlayerEnum): Boolean {
        return board.next === player
    }

    fun isPitHaveStones(board: Board, player: BoardPlayerEnum, pitIndex: Int): Boolean {
        return !board.getPlayerPits(player).isEmptyPit(pitIndex)
    }
}