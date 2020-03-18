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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private ChatChannelRepository chatChannelRepository;

    @Autowired
    public MessageController(MessageRepository messageRepository, UserRepository userRepository, ChatChannelRepository chatChannelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatChannelRepository = chatChannelRepository;
    }

    private User getWithUser(ChatChannel chatChannel, String username) {
        String withUsername = chatChannel.getUserNames().stream().filter(s -> !s.equals(username)).findFirst().orElse(null);
        Optional<User> optionalUser = userRepository.findByUsername(withUsername);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @GetMapping("/{idChatChannel}")
    public ResponseEntity<?> doGetAllMessageByIdChatChannel(@PathVariable String idChatChannel, Principal principal) {
        Optional<ChatChannel> optionalChatChannel = chatChannelRepository.findById(idChatChannel);
        if (!optionalChatChannel.isPresent()) {
            throw new NotFoundChatChannelException();
        }
        List<Message> messages = messageRepository.findByIdChatChannel(idChatChannel);

        User withUser = getWithUser(optionalChatChannel.get(), principal.getName());

        List<MessageResponse> messageResponses = messages.stream().map(message -> {
            int type = message.getTo().equals(withUser.getUsername()) ? 0 : 1 ;
            User user = (type == 0) ? null : withUser;
            return MessageResponse.createMessageResponse(type, message, user);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(messageResponses);
    }
}
