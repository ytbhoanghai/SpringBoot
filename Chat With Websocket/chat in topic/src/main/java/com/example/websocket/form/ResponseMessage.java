package com.example.websocket.form;

import com.example.websocket.entity.Message;
import com.example.websocket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseMessage {

    private String urlAvatar;
    private String fullName;
    private String content;
    private String dateSend;

    public static ResponseMessage createResponseMessage(Message message) {
        String _urlAvatar = message.getAuthor().getUrlAvatar();
        String _fullName = message.getAuthor().getFullName();
        String _content = message.getContent();
        String _dateSend = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(message.getDate());

        return new ResponseMessage(_urlAvatar, _fullName, _content, _dateSend);
    }
}
