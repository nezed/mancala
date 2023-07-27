package com.nezed.demo.mancala.model

import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import com.nezed.demo.mancala.model.data.BoardStateEnum
import com.nezed.demo.mancala.model.data.nextRandom

class Board(
    var state: BoardStateEnum = BoardStateEnum.NEW,
    var next: BoardPlayerEnum = BoardPlayerEnum.nextRandom(),
    val playerOnePits: PlayerPits = PlayerPits(),
    val playerTwoPits: PlayerPits = PlayerPits(),
)

fun Board.getPlayerPits(player: BoardPlayerEnum): PlayerPits {
    return if (player === BoardPlayerEnum.ONE) {
        playerOnePits
    } else {
        playerTwoPits
    }
}