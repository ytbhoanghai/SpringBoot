package com.example.serversentevent.controller;

import com.example.serversentevent.entity.RequestMessage;
import com.example.serversentevent.entity.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private static HashMap<String, SseEmitter> sseEmitters = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(value = "/listen/{username}")
    public SseEmitter sseEmitter(@PathVariable String username) {
        LOGGER.info(username + " connected");

        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitters.put(username, sseEmitter);

        return sseEmitter;
    }

    @RequestMapping(value = "/listen.close/{username}")
    public void closeSseEmitter(@PathVariable String username) {
        if (sseEmitters.containsKey(username)) {
            sseEmitters.remove(username);
            LOGGER.info(username + " disconnected");
        }
    }

    @PostMapping(value = "/new.message")
    public void broadcastMessages(@RequestBody RequestMessage requestMessage) throws JsonProcessingException {
        LOGGER.info(requestMessage.getFullName() + " send messages \"" + requestMessage.getContent() + "\"");

        ResponseMessage responseMessage = new ResponseMessage(requestMessage);
        String jsonResponseMessage = objectMapper.writeValueAsString(responseMessage);

        SseEmitter.SseEventBuilder builder = SseEmitter.event()
                .data(jsonResponseMessage, MediaType.APPLICATION_JSON)
                .reconnectTime(1000);

        sseEmitters.forEach((username, sseEmitter) -> {
            try {
                sseEmitter.send(builder);
                LOGGER.info("Server send message from user " + requestMessage.getFullName() + " to user " + username);
            } catch (IOException e) { e.printStackTrace(); }
        });
    }
}
