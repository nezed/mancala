package com.nezed.demo.mancala.model.response

import com.nezed.demo.mancala.model.Session
data class SessionResponse(
    val id: String,
    val board: BoardResponse,
)

fun Session.asSessionResponse(): SessionResponse {
    return SessionResponse(
        id.toHexString(),
        board.asBoardResponse(),
    )
}
