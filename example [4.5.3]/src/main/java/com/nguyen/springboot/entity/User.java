package com.nguyen.springboot.entity;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
public class User {

    @NotEmpty(message = "username can not be empty")
    private String username;
    @NotEmpty(message = "password can not be empty")
    private String password;
}
