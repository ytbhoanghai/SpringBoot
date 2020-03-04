package com.example.websocket.repository;

import com.example.websocket.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigInteger;

public interface MessageRepository extends MongoRepository<Message, BigInteger> {
}
