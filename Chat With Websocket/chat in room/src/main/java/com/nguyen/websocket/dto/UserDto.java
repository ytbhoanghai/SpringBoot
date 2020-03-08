package com.nguyen.websocket.dto;

import com.nguyen.websocket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;
    private String username;
    private String fullName;

    public static UserDto createUserDtoFromUser(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getFullName());
    }
}
