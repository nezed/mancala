package com.nezed.demo.mancala.repository

import com.nezed.demo.mancala.model.Session
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface SessionRepository : MongoRepository<Session, ObjectId>