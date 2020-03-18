package com.nguyen.demo.controller;

import com.nguyen.demo.entity.ChatChannel;
import com.nguyen.demo.entity.Message;
import com.nguyen.demo.entity.User;
import com.nguyen.demo.exception.ChatChannelIsExistsException;
import com.nguyen.demo.exception.UserNotFoundException;
import com.nguyen.demo.repository.ChatChannelRepository;
import com.nguyen.demo.repository.MessageRepository;
import com.nguyen.demo.repository.UserRepository;
import com.nguyen.demo.response.ChatChannelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/chatChannel")
public class ChatChannelController {

    private UserRepository userRepository;
    private ChatChannelRepository chatChannelRepository;
    private MessageRepository messageRepository;

    @Autowired
    public ChatChannelController(UserRepository userRepository, ChatChannelRepository chatChannelRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatChannelRepository = chatChannelRepository;
        this.messageRepository = messageRepository;
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    private User getCurrentUser(Principal principal) {
        return findUserByUsername(principal.getName());
    }

    private void saveInfo(ChatChannel chatChannel, User ...users) {
        chatChannelRepository.save(chatChannel);
        Arrays.stream(users).forEach(user -> {
            user.getIdChatChannels().add(chatChannel.getId());
            userRepository.save(user);
        });
    }

    private ChatChannel createChatChannel(User fromUser, User withUser) {
        String idChatChannel = UUID.randomUUID().toString();

        ChatChannel chatChannel = ChatChannel.builder()
                .id(idChatChannel)
                .userNames(Arrays.asList(fromUser.getUsername(), withUser.getUsername()))
                .createDate(new Date())
                .messageRecently(Message.getMessageDefault(idChatChannel, fromUser, withUser)).build();

        messageRepository.save(chatChannel.getMessageRecently());

        return chatChannel;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> createChatChannel(@RequestBody String withUsername, Principal principal) {
        User withUser = findUserByUsername(withUsername);
        if (withUser == null) { throw new UserNotFoundException(); }
        if (chatChannelRepository.findByUserNames(Arrays.asList(withUsername, principal.getName())).isPresent()) {
            throw new ChatChannelIsExistsException();
        }
        User currentUser = getCurrentUser(principal);

        ChatChannel chatChannel = createChatChannel(currentUser, withUser);
        saveInfo(chatChannel, withUser, currentUser);

        return ResponseEntity.ok(ChatChannelResponse.createChatChannelResponse(chatChannel, withUser));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllChatChannel(Principal principal) {
        String username = principal.getName();
        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        if (!optionalCurrentUser.isPresent()) {
            throw new UserNotFoundException();
        }

        List<ChatChannel> chatChannels = chatChannelRepository.findByListId(optionalCurrentUser.get().getIdChatChannels());
        List<ChatChannelResponse> chatChannelResponses = new ArrayList<>();

        for (ChatChannel chatChannel : chatChannels) {
            Optional<String> optionalWithUsername = chatChannel.getUserNames().stream()
                    .filter(s -> !s.equals(username))
                    .findFirst();

            if (optionalWithUsername.isPresent()) {
                Optional<User> optionalFromUser = userRepository.findByUsername(optionalWithUsername.get());
                if (!optionalFromUser.isPresent()) {
                    throw new UserNotFoundException();
                }
                chatChannelResponses.add(ChatChannelResponse.createChatChannelResponse(chatChannel, optionalFromUser.get()));
            }
        }

        return ResponseEntity.ok(chatChannelResponses);
    }

}
