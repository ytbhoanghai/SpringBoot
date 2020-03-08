package com.nguyen.websocket.dto;

import com.nguyen.websocket.entity.Message;
import com.nguyen.websocket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String id;
    private String urlAvatar;
    private String username;
    private String author;
    private String content;
    private String dateSend;
    private String idRoom;

    public static MessageDto createMessageDtoFromMessage(Message message, User user, String idRoom) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return new MessageDto(message.getId(), user.getUrlAvatar(), user.getUsername(), user.getFullName(), message.getContent(), format.format(message.getDateSend()), idRoom);
    }
}
