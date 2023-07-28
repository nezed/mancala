package com.nezed.demo.mancala.unit.helper

import com.nezed.demo.mancala.model.Board
import com.nezed.demo.mancala.model.PlayerPits
import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum

class BoardHelper {
    companion object {
        fun createWith(
            state: BoardStateEnum = BoardStateEnum.NEW,
            next: BoardPlayerEnum = BoardPlayerEnum.ONE,
            playerOnePits: PlayerPits = PlayerPits(),
            playerTwoPits: PlayerPits = PlayerPits(),
        ): Board {
            return Board(
                state,
                next,
                playerOnePits,
                playerTwoPits,
            )
        }

        fun createWithFirstPlayerMoved(): Board {
            return createWith(
                state = BoardStateEnum.IN_PROGRESS,
                playerOnePits = PlayerPits(
                    intArrayOf(0, 0, 8, 8, 8, 8),
                    2
                ),
                playerTwoPits = PlayerPits(
                    intArrayOf(7, 6, 6, 6, 6, 6),
                    0
                ),
                next = BoardPlayerEnum.TWO,
            )
        }


        fun createWithFirstPlayerMoveEndedInMancala(): Board {
            return createWith(
                state = BoardStateEnum.IN_PROGRESS,
                playerOnePits = PlayerPits(
                    intArrayOf(0, 7, 7, 7, 7, 7),
                    1
                ),
                playerTwoPits = PlayerPits(
                    intArrayOf(6, 6, 6, 6, 6, 6),
                    0
                ),
                next = BoardPlayerEnum.ONE,
            )
        }

        fun createFinishedBoard(): Board {
            return createWith(
                state = BoardStateEnum.FINISHED,
                playerOnePits = PlayerPits(
                    IntArray(6) { 0 },
                    35
                ),
                playerTwoPits = PlayerPits(
                    IntArray(6) { 0 },
                    37
                ),
            )
        }
    }
}