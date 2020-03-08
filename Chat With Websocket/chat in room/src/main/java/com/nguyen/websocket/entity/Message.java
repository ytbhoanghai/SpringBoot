package com.nguyen.websocket.entity;

import com.nguyen.websocket.dto.RoomDto;
import lombok.AllArgsConstructor;
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
@Document
@TypeAlias("message")
public class Message {

    @Id
    private String id;
    private String content;
    private String author;
    private String username;
    private Date dateSend;
    private RoomDto room;

    public static Message createMessage(String content, User user, Room room) {
        RoomDto roomDto = RoomDto.createRoomDtoFromRoom(room);
        return new Message(UUID.randomUUID().toString(), content, user.getFullName(), user.getUsername(), new Date(), roomDto);
    }
}
