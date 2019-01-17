package me.rezabayat.pigdice.controller;

import me.rezabayat.pigdice.dto.UserDTO;
import me.rezabayat.pigdice.service.WebSocketSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class WebSocketController {

    private final WebSocketSender webSocketSender;

    public WebSocketController(WebSocketSender webSocketSender) {
        this.webSocketSender = webSocketSender;
    }

    @MessageMapping("/start")
    public void start(@Header("simpSessionId") String sessionId  , UserDTO user)  {
        System.out.println(user);
        this.webSocketSender.sendOnlineUserToAll(sessionId, user);
        this.webSocketSender.sendOnlineUsers(sessionId);
    }
}
