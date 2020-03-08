package com.nguyen.websocket.controller;

import com.nguyen.websocket.dto.MessageDto;
import com.nguyen.websocket.entity.Message;
import com.nguyen.websocket.entity.User;
import com.nguyen.websocket.repository.MessageRepository;
import com.nguyen.websocket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/message")
public class MessageController {

    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllMessageInRoom(@PathVariable String id) {
        List<MessageDto> listMessageDto = new ArrayList<>();
        List<Message> messages = messageRepository.findByIdRoom(id);
        for (Message message : messages) {
            Optional<User> optionalUser = userRepository.findByUsername(message.getUsername());
            if (!optionalUser.isPresent()) { continue; }
            listMessageDto.add(MessageDto.createMessageDtoFromMessage(message, optionalUser.get(), id));
        }
        return ResponseEntity.ok(listMessageDto);
    }
}
