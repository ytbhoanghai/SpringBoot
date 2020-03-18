package com.nguyen.demo.controller;

import com.nguyen.demo.entity.User;
import com.nguyen.demo.repository.UserRepository;
import com.nguyen.demo.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doGetUsers(Principal principal) {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .filter(user -> !principal.getName().equals(user.getUsername()))
                .map(UserResponse::createUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }
}
