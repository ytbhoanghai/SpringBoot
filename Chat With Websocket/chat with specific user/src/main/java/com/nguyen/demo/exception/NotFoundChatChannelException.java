package com.nguyen.demo.exception;

public class NotFoundChatChannelException extends RuntimeException {

    public NotFoundChatChannelException() {
        super("Không tìm thấy kênh chat");
    }
}
