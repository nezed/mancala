package com.nezed.demo.mancala.model.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.PositiveOrZero

data class SessionMoveDto (
    @field:Max(5)
    @field:PositiveOrZero
    val pitIndex: Int
)
