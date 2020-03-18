package com.nguyen.demo.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Không tìm thấy User này trong hệ thống");
    }

}
