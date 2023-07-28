package com.nezed.demo.mancala.unit.service

import com.nezed.demo.mancala.model.PlayerPits
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.service.AssertService
import com.nezed.demo.mancala.service.BoardService
import com.nezed.demo.mancala.service.BoardValidationService
import com.nezed.demo.mancala.unit.helper.BoardHelper
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals

class BoardServiceUnitTests {
    @SpyK
    var assertService = AssertService()

    @SpyK
    var boardValidationService = BoardValidationService(assertService)

    @SpyK
    var boardService = BoardService(boardValidationService)

    @BeforeEach
    fun setUp() = MockKAnnotations.init(this, relaxUnitFun = true)

    @Test
    fun whenMoveEndsOnOpponentSide_thenOpponentGotStones() {
        val board = BoardHelper.createWith(next = BoardPlayerEnum.ONE)

        boardService.move(board, BoardPlayerEnum.ONE, 5)

        assertArrayEquals(intArrayOf(6, 6, 6, 6, 6, 0), board.playerOnePits.pits)
        assertEquals(1, board.playerOnePits.mancala)
        assertArrayEquals(intArrayOf(7, 7, 7, 7, 7, 6), board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardPlayerEnum.TWO, board.next)
    }

    @Test
    fun whenMoveEndsInMancala_thenAllowedToSecondMove() {
        val board = BoardHelper.createWith(next = BoardPlayerEnum.ONE)

        boardService.move(board, BoardPlayerEnum.ONE, 0)

        assertArrayEquals(intArrayOf(0, 7, 7, 7, 7, 7), board.playerOnePits.pits)
        assertEquals(1, board.playerOnePits.mancala)
        assertArrayEquals(IntArray(6) { 6 }, board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardPlayerEnum.ONE, board.next)
    }

    @Test
    fun whenMoveGoesOverTheBoardFewTimes_thenOpponentsMancalaShouldntBeChanged() {
        val board = BoardHelper.createWith(
            next = BoardPlayerEnum.ONE,
            playerOnePits = PlayerPits(intArrayOf(0, 0, 0, 0, 0, 14), 0),
            playerTwoPits = PlayerPits(IntArray(6) { 0 }, 0),
        )

        boardService.move(board, BoardPlayerEnum.ONE, 5)

        assertArrayEquals(IntArray(6) { 1 }, board.playerOnePits.pits)
        assertEquals(2, board.playerOnePits.mancala)
        assertArrayEquals(IntArray(6) { 1 }, board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardPlayerEnum.ONE, board.next)
    }

    @Test
    fun whenMoveEndsInOwnEmptyPit_thenOpponentsPitCaptured() {
        val board = BoardHelper.createWith(
            next = BoardPlayerEnum.ONE,
            playerOnePits = PlayerPits(intArrayOf(0, 2, 0, 0, 0, 0), 0),
            playerTwoPits = PlayerPits(intArrayOf(1, 2, 3, 4, 5, 6), 0),
        )

        boardService.move(board, BoardPlayerEnum.ONE, 1)

        assertArrayEquals(intArrayOf(0, 0, 1, 0, 0, 0), board.playerOnePits.pits)
        assertEquals(3 + 1, board.playerOnePits.mancala)
        assertArrayEquals(intArrayOf(1, 2, 0, 4, 5, 6), board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardPlayerEnum.TWO, board.next)
    }

    @Test
    fun whenMoveEndsInOpponentsEmptyPit_thenShoudntBeCaptured() {
        val board = BoardHelper.createWith(
            next = BoardPlayerEnum.ONE,
            playerOnePits = PlayerPits(intArrayOf(0, 6, 0, 0, 0, 0), 0),
            playerTwoPits = PlayerPits(intArrayOf(0, 1, 2, 3, 4, 5), 0),
        )

        boardService.move(board, BoardPlayerEnum.ONE, 1)

        assertArrayEquals(intArrayOf(0, 0, 1, 1, 1, 1), board.playerOnePits.pits)
        assertEquals(1, board.playerOnePits.mancala)
        assertArrayEquals(intArrayOf(1, 1, 2, 3, 4, 5), board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardPlayerEnum.TWO, board.next)
    }

    @Test
    fun whenPlayerHaveNoMoreStones_thenGameEnds() {
        val board = BoardHelper.createWith(
            next = BoardPlayerEnum.ONE,
            playerOnePits = PlayerPits(intArrayOf(0, 0, 0, 0, 0, 1), 0),
            playerTwoPits = PlayerPits(intArrayOf(1, 2, 3, 4, 5, 6), 0),
        )

        boardService.move(board, BoardPlayerEnum.ONE, 5)

        assertArrayEquals(IntArray(6) { 0 }, board.playerOnePits.pits)
        assertEquals(1, board.playerOnePits.mancala)
        assertArrayEquals(IntArray(6) { 0 }, board.playerTwoPits.pits)
        assertEquals(21, board.playerTwoPits.mancala)
        assertEquals(BoardStateEnum.FINISHED, board.state)
    }

    @Test
    fun whenLastOpponentsStoneCaptured_thenGameEnds() {
        val board = BoardHelper.createWith(
            next = BoardPlayerEnum.ONE,
            playerOnePits = PlayerPits(intArrayOf(0, 0, 0, 2, 0, 0), 0),
            playerTwoPits = PlayerPits(intArrayOf(1, 0, 0, 0, 0, 0), 0),
        )

        boardService.move(board, BoardPlayerEnum.ONE, 3)

        assertArrayEquals(IntArray(6) { 0 }, board.playerOnePits.pits)
        assertEquals(3, board.playerOnePits.mancala)
        assertArrayEquals(IntArray(6) { 0 }, board.playerTwoPits.pits)
        assertEquals(0, board.playerTwoPits.mancala)
        assertEquals(BoardStateEnum.FINISHED, board.state)
    }
}