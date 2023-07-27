package com.nezed.demo.mancala.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class BoardFinishedException(private val customMessage: String = "Game at the board has been already finished") : ResponseStatusException(HttpStatus.BAD_REQUEST, customMessage)
