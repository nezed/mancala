package com.nezed.demo.mancala.model.data

import kotlin.random.Random

enum class BoardPlayerEnum {
    ONE, TWO;

    companion object {}
}

fun BoardPlayerEnum.getOpposite(): BoardPlayerEnum {
    return when (this) {
        BoardPlayerEnum.ONE -> BoardPlayerEnum.TWO
        else -> BoardPlayerEnum.ONE
    }
}

fun BoardPlayerEnum.Companion.nextRandom(): BoardPlayerEnum {
    return if (Random.nextBoolean()) {
        BoardPlayerEnum.ONE
    } else {
        BoardPlayerEnum.TWO
    }
}
