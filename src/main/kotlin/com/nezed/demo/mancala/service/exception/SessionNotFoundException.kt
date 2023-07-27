package com.nezed.demo.mancala.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SessionNotFoundException(private val customMessage: String = "Not found") : ResponseStatusException(HttpStatus.NOT_FOUND, customMessage)
