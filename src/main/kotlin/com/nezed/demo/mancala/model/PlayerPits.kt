package com.nezed.demo.mancala.model

class PlayerPits(
    var pits: IntArray = IntArray(6) { 6 },
    var mancala: Int = 0,
)

fun PlayerPits.isEmptyPit(pit: Int): Boolean {
    return pits[pit] == 0
}

fun PlayerPits.canMove(): Boolean {
    return pits.sum() > 0
}

fun PlayerPits.grabStonesFromPit(pit: Int): Int {
    val stones = pits[pit]
    pits[pit] = 0
    return stones
}

fun PlayerPits.putStoneToPit(pit: Int) {
    pits[pit]++
}

fun PlayerPits.putStonesToMancala(stones: Int) {
    mancala += stones
}

fun PlayerPits.finish() {
    putStonesToMancala(pits.sum())
    pits.fill(0)
}
