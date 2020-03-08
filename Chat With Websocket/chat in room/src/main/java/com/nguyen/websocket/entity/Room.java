package com.nguyen.websocket.entity;

import com.nguyen.websocket.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@TypeAlias("room")
public class Room {

    @Id
    private String id;
    private String name;
    private List<UserDto> users;
}
