package com.nezed.demo.mancala

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MancalaApplication

fun main(args: Array<String>) {
	runApplication<MancalaApplication>(*args)
}
