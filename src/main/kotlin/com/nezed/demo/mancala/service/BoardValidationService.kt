package com.nezed.demo.mancala.service

import com.nezed.demo.mancala.model.Board
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.model.getPlayerPits
import com.nezed.demo.mancala.model.isEmptyPit
import com.nezed.demo.mancala.service.exception.BoardFinishedException
import com.nezed.demo.mancala.service.exception.RulesViolationException
import org.springframework.stereotype.Service

@Service
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

    private fun isGameFinished(board: Board): Boolean {
        return board.state === BoardStateEnum.FINISHED
    }

    private fun isPlayerNext(board: Board, player: BoardPlayerEnum): Boolean {
        return board.next === player
    }

    private fun isPitHaveStones(board: Board, player: BoardPlayerEnum, pitIndex: Int): Boolean {
        return !board.getPlayerPits(player).isEmptyPit(pitIndex)
    }
}