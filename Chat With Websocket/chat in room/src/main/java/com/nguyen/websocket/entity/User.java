package com.nguyen.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@TypeAlias("user")
public class User {

    @Id
    private String id;
    private String fullName;
    private String username;
    private String password;
    private String urlAvatar;
    private String role;
}
