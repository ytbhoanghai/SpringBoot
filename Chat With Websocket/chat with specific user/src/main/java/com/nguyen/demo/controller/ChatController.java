package com.nguyen.demo.controller;

import com.nguyen.demo.entity.ChatChannel;
import com.nguyen.demo.entity.Message;
import com.nguyen.demo.entity.User;
import com.nguyen.demo.exception.NotFoundChatChannelException;
import com.nguyen.demo.exception.UserNotFoundException;
import com.nguyen.demo.repository.ChatChannelRepository;
import com.nguyen.demo.repository.MessageRepository;
import com.nguyen.demo.repository.UserRepository;
import com.nguyen.demo.response.MessageResponse;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ChatController {

    private ChatChannelRepository chatChannelRepository;
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(ChatChannelRepository chatChannelRepository, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository, MessageRepository messageRepository) {
        this.chatChannelRepository = chatChannelRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/sendMessage.to/{idChatChannel}")
    public void receiveMessageFromClient(@DestinationVariable String idChatChannel, String content, Principal principal) {
        Optional<ChatChannel> optionalChatChannel = chatChannelRepository.findById(idChatChannel);
        if (!optionalChatChannel.isPresent()) {
            throw new NotFoundChatChannelException();
        }
        ChatChannel chatChannel = optionalChatChannel.get();
        String fromUsername = principal.getName();
        String toUsername = chatChannel.getUserNames().stream().filter(s -> !s.equals(fromUsername)).findFirst().orElse(null);

        Message message = Message.builder()
                .id(UUID.randomUUID().toString())
                .from(fromUsername)
                .to(toUsername)
                .content(content)
                .sendDate(new Date())
                .idChatChannel(idChatChannel)
                .build();

        messageRepository.save(message);
        chatChannel.setMessageRecently(message);
        chatChannelRepository.save(chatChannel);

        sendMessage(message);
    }

    private void sendMessage(Message message) {
        Optional<User> optionalUser = userRepository.findByUsername(message.getFrom());
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }

        MessageResponse messageResponseForSender = MessageResponse.createMessageResponse(0, message, null);
        MessageResponse messageResponseForReceive = MessageResponse.createMessageResponse(1, message, optionalUser.get());

        simpMessagingTemplate.convertAndSendToUser(message.getFrom(), "/queue/reply", messageResponseForSender);
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/queue/reply", messageResponseForReceive);
    }
}
