package com.nguyen.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
@TypeAlias("message")
public class Message {

    @Id
    private String id;
    private String from;
    private String to;
    private String content;
    private Date sendDate;
    private String idChatChannel;

    public static Message getMessageDefault(String idChatChannel, User fromUser, User withUser) {
        return Message.builder()
                .id(UUID.randomUUID().toString())
                .from(fromUser.getUsername())
                .to(withUser.getUsername())
                .content(fromUser.getFullName() + " Created Room Chat With " + withUser.getFullName())
                .sendDate(new Date())
                .idChatChannel(idChatChannel).build();
    }
}
