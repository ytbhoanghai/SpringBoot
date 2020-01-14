package com.nguyen.i18n.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class User {

    @NotEmpty(message = "{validations.userName.notEmpty}")
    private String userName;
    @NotEmpty(message = "{validations.password.notEmpty}")
    private String password;
}
