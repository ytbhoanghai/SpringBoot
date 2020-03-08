package com.nguyen.websocket;

import com.nguyen.websocket.entity.Room;
import com.nguyen.websocket.entity.User;
import com.nguyen.websocket.repository.RoomRepository;
import com.nguyen.websocket.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebsocketApplication.class, args);
    }

}
