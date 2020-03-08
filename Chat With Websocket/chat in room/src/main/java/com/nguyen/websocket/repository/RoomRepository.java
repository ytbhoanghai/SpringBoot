package com.nguyen.websocket.repository;

import com.nguyen.websocket.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

    @Query(value = "{\"users.username\": ?0}")
    List<Room> findByUsername(String username);

    @Query(value = "{name: ?0, \"users.username\": ?1}")
    Optional<Room> findByNameAndUsername( String name, String username);

    Optional<Room> findById(String id);
}
