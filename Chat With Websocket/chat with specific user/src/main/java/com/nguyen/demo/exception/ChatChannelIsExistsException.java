package com.nguyen.demo.exception;

public class ChatChannelIsExistsException extends RuntimeException {

    public ChatChannelIsExistsException() {
        super("Kênh Chat với người này đã tồn tại");
    }
}
