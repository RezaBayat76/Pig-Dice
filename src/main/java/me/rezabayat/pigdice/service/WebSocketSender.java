package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dto.UserDTO;
import me.rezabayat.pigdice.dto.UsersDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WebSocketSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, UserDTO> onlineUsers = new HashMap<>();


    public WebSocketSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessageToAll(String message) {
        try {
            this.messagingTemplate.convertAndSend("/topic/hi", message);
        } catch (Exception ignore) {

        }
    }

    public void sendMessage(String sessionId, String message) {
        try {
            this.messagingTemplate.convertAndSendToUser(sessionId, "/topic/message", message);
        } catch (Exception ignore) {

        }
    }


    public void sendNewUserToAll(UserDTO userDTO) {
        try {

            this.messagingTemplate.convertAndSend("/topic/new-user", userDTO);
        } catch (Exception ignore) {

        }
    }


    public void sendOnlineUserToAll(String sessionId, UserDTO userDTO) {
        try {
            this.onlineUsers.put(sessionId, userDTO);
            this.messagingTemplate.convertAndSend("/topic/user-online", userDTO);
        } catch (Exception ignore) {

        }
    }

    public void sendOfflineUserToAll(String sessionId) {
        try {
            UserDTO userDTO = this.onlineUsers.remove(sessionId);
            this.messagingTemplate.convertAndSend("/topic/user-offline", userDTO);
        } catch (Exception ignore) {

        }
    }

    public void sendOnlineUsers(String sessionId) {
        try {
            List<UserDTO> users = this.onlineUsers.values().stream().collect(Collectors.toList());
            System.out.println(users);
            if (!users.isEmpty()){
                UsersDTO usersDTO = new UsersDTO();
                usersDTO.setUsers(users);
                this.messagingTemplate.convertAndSendToUser(sessionId, "/user/online-users", usersDTO);

            }
        } catch (Exception ignore) {

        }
    }
}
