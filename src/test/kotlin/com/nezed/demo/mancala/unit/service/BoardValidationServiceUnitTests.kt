package com.nezed.demo.mancala.unit.service

import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.service.AssertService
import com.nezed.demo.mancala.service.BoardValidationService
import com.nezed.demo.mancala.service.exception.BoardFinishedException
import com.nezed.demo.mancala.service.exception.RulesViolationException
import com.nezed.demo.mancala.unit.helper.BoardHelper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class BoardValidationServiceUnitTests {
    @SpyK
    var assertService = AssertService()

    @SpyK
    var boardValidationService = BoardValidationService(assertService)

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun whenFirstMove_thenNothing() {
        val board = BoardHelper.createWith()

        assertDoesNotThrow {
            boardValidationService.validateMove(board, BoardPlayerEnum.ONE, 0)
        }
    }

    @Test
    fun whenMoveOnFinishedBoard_thenThrow() {
        val board = BoardHelper.createFinishedBoard()

        assertThrows<BoardFinishedException> {
            boardValidationService.validateMove(board, board.next, 0)
        }
    }

    @Test
    fun whenMoveOutOfOrder_thenThrow() {
        val board = BoardHelper.createWithFirstPlayerMoved()

        assertThrows<RulesViolationException> {
            boardValidationService.validateMove(board, BoardPlayerEnum.ONE, 5)
        }
    }

    @Test
    fun whenMoveAtEmptyPit_thenThrow() {
        val board = BoardHelper.createWithFirstPlayerMoveEndedInMancala()

        assertThrows<RulesViolationException> {
            boardValidationService.validateMove(board, BoardPlayerEnum.ONE, 0)
        }
    }
}