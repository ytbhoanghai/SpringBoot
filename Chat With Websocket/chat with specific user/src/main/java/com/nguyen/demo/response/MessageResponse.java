package com.nguyen.demo.response;

import com.nguyen.demo.entity.Message;
import com.nguyen.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private Integer type;
    private String content;
    private String dateSend;
    private String urlAvatar;
    private String fullName;
    private String idChatChannel;

    public static MessageResponse createMessageResponse(Integer type, Message message, User toUser) {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a | MMM dd");
        String urlAvatar = (toUser != null) ? toUser.getUrlAvatar() : "";
        String fullName = (toUser != null) ? toUser.getFullName() : "";

        return new MessageResponse(type, message.getContent(), format.format(message.getSendDate()), urlAvatar, fullName, message.getIdChatChannel());
    }

}
