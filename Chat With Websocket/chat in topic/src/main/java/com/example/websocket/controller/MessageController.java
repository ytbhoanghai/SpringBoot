package com.example.websocket.controller;

import com.example.websocket.entity.Message;
import com.example.websocket.form.ResponseMessage;
import com.example.websocket.repository.MessageRepository;
import com.example.websocket.security.CustomUserDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    private MessageRepository messageRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @MessageMapping(value = "/messages")
    @SendTo(value = "/topic/messages")
    public ResponseMessage doGetProcessMessage(String content, Authentication authentication) {
        LOGGER.info("Server received message with content is: " + content);
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        Message message = Message.createMessage(content, userDetails.user);
        messageRepository.save(message);

        return ResponseMessage.createResponseMessage(message);
    }

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetAllResponseMessage() {
        List<ResponseMessage> messageList = messageRepository.findAll(Sort.by("date")).stream()
                .map(ResponseMessage::createResponseMessage)
                .collect(Collectors.toList());
        return ResponseEntity.ok(messageList);
    }
}
