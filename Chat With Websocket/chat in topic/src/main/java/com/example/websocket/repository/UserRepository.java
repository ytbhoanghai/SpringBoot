package com.example.websocket.repository;

import com.example.websocket.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, BigInteger> {

    Optional<User> findByUsername(String username);
}
