package com.nezed.demo.mancala.service

import com.nezed.demo.mancala.model.*
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.model.data.getOpposite
import org.springframework.stereotype.Service

private const val PITS_CONT = 6

@Service
class BoardService(
    private val boardValidationService: BoardValidationService,
) {
    fun move(board: Board, movePlayer: BoardPlayerEnum, movePit: Int) {
        boardValidationService.validateMove(board, movePlayer, movePit)

        var currentPit = PitLocator(movePlayer, movePit + 1)
        var stones = board.getPlayerPits(movePlayer).grabStonesFromPit(movePit)
        while (stones > 1) {
            putPit(board, currentPit)
            stones--
            currentPit = nextPit(movePlayer, currentPit)
        }
        if (stones == 1) {
            putLastToPit(board, movePlayer, currentPit)
        }

        board.next = getNextPlayer(movePlayer, currentPit)
        board.state = BoardStateEnum.IN_PROGRESS
        if(!board.playerOnePits.canMove() || !board.playerTwoPits.canMove()) {
            board.playerOnePits.finish()
            board.playerTwoPits.finish()
            board.state = BoardStateEnum.FINISHED
        }
    }

    private fun putPit(board: Board, pit: PitLocator, stones: Int = 1) {
        val playerSide = board.getPlayerPits(pit.playerSide)
        if (pit.isMancala()) {
            playerSide.putStonesToMancala(stones)
        } else {
            playerSide.putStoneToPit(pit.pitIndex)
        }
    }

    private fun putLastToPit(board: Board, movePlayer: BoardPlayerEnum, pit: PitLocator) {
        val playerSide = board.getPlayerPits(pit.playerSide)
        if (!pit.isMancala() && playerSide.isEmptyPit(pit.pitIndex) && movePlayer === pit.playerSide) {
            captureOpposite(board, movePlayer, pit)
            playerSide.putStonesToMancala(1)
        } else {
            putPit(board, pit)
        }
    }

    private fun nextPit(movePlayer: BoardPlayerEnum, currentPit: PitLocator): PitLocator {
        if (currentPit.isMancala()) {
            return PitLocator(currentPit.playerSide.getOpposite(), 0)
        }
        if (currentPit.playerSide !== movePlayer && currentPit.pitIndex == PITS_CONT-1) {
            return PitLocator(movePlayer, 0)
        }
        return currentPit.copy(pitIndex = currentPit.pitIndex + 1)
    }

    private fun captureOpposite(board: Board, movePlayer: BoardPlayerEnum, pit: PitLocator) {
        val oppositeLocator = pit.toOppositePit()
        val stones = board.getPlayerPits(oppositeLocator.playerSide).grabStonesFromPit(oppositeLocator.pitIndex)
        board.getPlayerPits(movePlayer).putStonesToMancala(stones)
    }

    private fun getNextPlayer(movePlayer: BoardPlayerEnum, lastPit: PitLocator): BoardPlayerEnum {
        return if (lastPit.playerSide == movePlayer && lastPit.isMancala()) {
            movePlayer
        } else {
            movePlayer.getOpposite()
        }
    }

    private data class PitLocator(
        val playerSide: BoardPlayerEnum,
        val pitIndex: Int,
    ) {
        fun isMancala(): Boolean {
            return pitIndex == PITS_CONT
        }

        fun toOppositePit(): PitLocator {
            return PitLocator(
                playerSide.getOpposite(),
                PITS_CONT - 1 - pitIndex,
            )
        }
    }
}