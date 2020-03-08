package com.nguyen.websocket.controller;

import com.nguyen.websocket.dto.MessageDto;
import com.nguyen.websocket.entity.Message;
import com.nguyen.websocket.entity.Room;
import com.nguyen.websocket.entity.User;
import com.nguyen.websocket.exception.NotFoundRoomException;
import com.nguyen.websocket.repository.MessageRepository;
import com.nguyen.websocket.repository.RoomRepository;
import com.nguyen.websocket.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class ChatController {

    private SimpMessagingTemplate simpMessagingTemplate;

    private RoomRepository roomRepository;
    private MessageRepository messageRepository;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, RoomRepository roomRepository, MessageRepository messageRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
    }

    @MessageMapping(value = "/chat.sendMessage/{id}")
    public void receiveMessageFromUser(@DestinationVariable String id, String content, Authentication auth) {
        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (!optionalRoom.isPresent()) {
            throw new NotFoundRoomException();
        }

        Message message = Message.createMessage(content, user, optionalRoom.get());
        messageRepository.save(message);

        simpMessagingTemplate.convertAndSend("/topic/room/" + id, MessageDto.createMessageDtoFromMessage(message, user, id));
    }
}
