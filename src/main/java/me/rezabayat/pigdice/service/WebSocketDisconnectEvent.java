package me.rezabayat.pigdice.service;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
public class WebSocketDisconnectEvent implements ApplicationListener<SessionDisconnectEvent> {

    private final WebSocketSender webSocketSender;

    public WebSocketDisconnectEvent(WebSocketSender webSocketSender) {
        this.webSocketSender = webSocketSender;
    }

    public void onApplicationEvent(SessionDisconnectEvent event) {
        System.out.println("disconnect event [sessionId: " + event.getSessionId() + " ]");
        this.webSocketSender.sendOfflineUserToAll(event.getSessionId());
    }
}