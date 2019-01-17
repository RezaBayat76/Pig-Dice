package me.rezabayat.pigdice.service;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;


@Component
public class WebSocketConnectEvent implements ApplicationListener<SessionConnectEvent> {

    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("Connect event sessionId: " + sha.getSessionId());
    }
}
