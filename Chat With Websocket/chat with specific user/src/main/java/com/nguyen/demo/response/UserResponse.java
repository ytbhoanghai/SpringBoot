package com.nguyen.demo.response;

import com.nguyen.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String username;
    private String fullName;

    public static UserResponse createUserResponse(User user) {
        return new UserResponse(user.getUsername(), user.getFullName());
    }
}
