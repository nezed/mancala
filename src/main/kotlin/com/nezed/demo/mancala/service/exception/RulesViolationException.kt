package com.nezed.demo.mancala.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class RulesViolationException(private val customMessage: String = "Such move is violating game rules") : ResponseStatusException(HttpStatus.BAD_REQUEST, customMessage)
