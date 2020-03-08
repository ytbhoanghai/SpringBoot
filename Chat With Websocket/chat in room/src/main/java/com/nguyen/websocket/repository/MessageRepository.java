package com.nguyen.websocket.repository;

import com.nguyen.websocket.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    @Query(value = "{\"room.id\": ?0}")
    List<Message> findByIdRoom(String id);
}
