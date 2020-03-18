package com.nguyen.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@TypeAlias("user")
public class User {

    @Id
    private String id;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private String urlAvatar;
    private List<String> idChatChannels;
}
