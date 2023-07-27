package com.nezed.demo.mancala.service

import org.springframework.stereotype.Service

@Service
class AssertService {
    fun isTrue(expression: Boolean, constructException: () -> Exception) {
        if (!expression) {
            throw constructException()
        }
    }
}