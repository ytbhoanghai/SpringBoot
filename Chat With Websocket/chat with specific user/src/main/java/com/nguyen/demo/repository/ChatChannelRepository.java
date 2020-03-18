package com.nguyen.demo.repository;

import com.nguyen.demo.entity.ChatChannel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatChannelRepository extends MongoRepository<ChatChannel, String> {

    @Query(value = "{userNames: {$all: ?0}}")
    Optional<ChatChannel> findByUserNames(List<String> userNames);

    @Query(value = "{_id: {$in: ?0}}")
    List<ChatChannel> findByListId(List<String> ids);

}

