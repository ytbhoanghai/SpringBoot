package com.nguyen.websocket.dto;

import com.nguyen.websocket.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private String id;
    private String name;

    public static RoomDto createRoomDtoFromRoom(Room room) {
        return new RoomDto(room.getId(), room.getName());
    }
}
