package com.nezed.demo.mancala.model.response

import com.nezed.demo.mancala.model.PlayerPits

class PlayerPitsResponse(
    val pits: IntArray,
    val mancala: Int,
)

fun PlayerPits.asPlayerPitsResponse(): PlayerPitsResponse {
    return PlayerPitsResponse(
        pits,
        mancala,
    )
}
