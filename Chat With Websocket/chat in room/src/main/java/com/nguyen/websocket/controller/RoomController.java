package com.nguyen.websocket.controller;

import com.nguyen.websocket.dto.RoomDto;
import com.nguyen.websocket.dto.UserDto;
import com.nguyen.websocket.entity.Message;
import com.nguyen.websocket.entity.Room;
import com.nguyen.websocket.entity.User;
import com.nguyen.websocket.exception.DuplicateNameRoomException;
import com.nguyen.websocket.exception.NotFoundRoomException;
import com.nguyen.websocket.repository.RoomRepository;
import com.nguyen.websocket.repository.UserRepository;
import com.nguyen.websocket.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/room")
public class RoomController {

    private RoomRepository roomRepository;

    @Autowired
    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoom(Authentication auth) {
        String username = auth.getName();
        List<RoomDto> rooms = roomRepository.findByUsername(username).stream()
                .map(RoomDto::createRoomDtoFromRoom)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rooms);
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> registerRoom(@RequestBody String name, Authentication auth) {
        if (roomRepository.findByNameAndUsername(name, auth.getName()).isPresent()) {
            throw new DuplicateNameRoomException();
        }

        User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
        Room room = new Room(UUID.randomUUID().toString(), name, new ArrayList<>());
        addUseToRoom(user, room);

        return ResponseEntity.ok(room.getId());
    }

    @PutMapping(value = "/join", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> joinRoom(@RequestBody String id, Authentication auth) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            User user = ((CustomUserDetails) auth.getPrincipal()).getUser();
            Room room = optionalRoom.get();

            if (isExistInRoom(room, user.getUsername())) {
                throw new DuplicateNameRoomException();
            }

            addUseToRoom(user, room);

            return ResponseEntity.ok(RoomDto.createRoomDtoFromRoom(room));
        }
        throw new NotFoundRoomException();
    }

    private void addUseToRoom(User user, Room room) {
        UserDto userDto = UserDto.createUserDtoFromUser(user);
        room.getUsers().add(userDto);

        roomRepository.save(room);
    }

    private boolean isExistInRoom(Room room, String username) {
        return room.getUsers().stream().map(UserDto::getUsername).anyMatch(s -> s.equals(username));
    }
}
