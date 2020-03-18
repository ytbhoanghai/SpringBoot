package com.nguyen.demo.response;

import com.nguyen.demo.entity.ChatChannel;
import com.nguyen.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatChannelResponse {

    private String idChatChannel;
    private String urlAvatar;
    private String fullName;
    private String username;
    private String contentRecently;
    private String dateSeenRecently;

    public static ChatChannelResponse createChatChannelResponse(ChatChannel chatChannel, User user) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM");
        return ChatChannelResponse.builder()
                .idChatChannel(chatChannel.getId())
                .urlAvatar(user.getUrlAvatar())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .contentRecently(chatChannel.getMessageRecently().getContent())
                .dateSeenRecently(format.format(chatChannel.getMessageRecently().getSendDate())).build();
    }
}
