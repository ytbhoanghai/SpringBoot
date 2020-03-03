package com.example.serversentevent.entity;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class ResponseMessage {

    private static String DEFAULT_URL_AVATAR = "http://127.0.0.1:8080/images/user_1.jpg";

    private String urlAvatar;
    private String fullName;
    private String content;
    private String dateSend;

    public ResponseMessage(RequestMessage requestMessage) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        this.urlAvatar = DEFAULT_URL_AVATAR;
        this.fullName = requestMessage.getFullName();
        this.content = requestMessage.getContent();
        this.dateSend = dateFormat.format(new Date());
    }
}
