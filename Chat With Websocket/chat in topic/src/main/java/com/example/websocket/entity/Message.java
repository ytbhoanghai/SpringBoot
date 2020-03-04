package com.example.websocket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Message {

    @Id
    private BigInteger id;
    private String content;
    private Date date;
    @DBRef
    private User author;

    public static Message createMessage(String content, User author) {
        return new Message(null, content, new Date(), author);
    }
}
