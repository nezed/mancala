package com.nezed.demo.mancala.service.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class SessionFullException(private val customMessage: String = "No more players can join this session") : ResponseStatusException(HttpStatus.FORBIDDEN, customMessage)
