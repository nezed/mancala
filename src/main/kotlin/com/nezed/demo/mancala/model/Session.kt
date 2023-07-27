package com.nezed.demo.mancala.model

import com.nezed.demo.mancala.model.data.BoardPlayerEnum
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "session")
class Session(
    @Id
    val id: ObjectId = ObjectId(),
    var board: Board = Board(),
    private val playerOneSessionId: String,
    private var playerTwoSessionId: String? = null,
    @Indexed(name = "ttl_index", expireAfterSeconds = 24 * 60 * 60)
    private val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun isJoinable(): Boolean {
        return playerTwoSessionId == null
    }

    fun join(sessionId: String) {
        playerTwoSessionId = sessionId
    }
    fun joinedAs(sessionId: String): BoardPlayerEnum? {
        return when (sessionId) {
            playerOneSessionId -> BoardPlayerEnum.ONE
            playerTwoSessionId -> BoardPlayerEnum.TWO
            else -> null
        }
    }
}
