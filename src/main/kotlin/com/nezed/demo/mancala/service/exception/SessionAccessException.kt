package com.nezed.demo.mancala.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SessionAccessException(private val customMessage: String = "You are not a member of this session") : ResponseStatusException(HttpStatus.FORBIDDEN, customMessage)
