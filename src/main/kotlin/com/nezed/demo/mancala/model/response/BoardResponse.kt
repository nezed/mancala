package com.nezed.demo.mancala.model.response

import com.nezed.demo.mancala.model.Board
import com.nezed.demo.mancala.model.data.BoardPlayerEnum

data class BoardResponse(
    val state: String,
    val playerOneSide: PlayerPitsResponse,
    val playerTwoSide: PlayerPitsResponse,
    val nextPlayer: BoardPlayerEnum,
    val leader: BoardPlayerEnum?,
)

fun Board.asBoardResponse(): BoardResponse {
    val playerOnePitsResponse = playerOnePits.asPlayerPitsResponse()
    val playerTwoPitsResponse = playerTwoPits.asPlayerPitsResponse()
    val leader = if (playerOnePitsResponse.mancala > playerTwoPitsResponse.mancala) {
        BoardPlayerEnum.ONE
    } else if (playerOnePitsResponse.mancala == playerTwoPitsResponse.mancala) {
        null
    } else {
        BoardPlayerEnum.TWO
    }

    return BoardResponse(
        state.name,
        playerOnePitsResponse,
        playerTwoPitsResponse,
        next,
        leader,
    )
}