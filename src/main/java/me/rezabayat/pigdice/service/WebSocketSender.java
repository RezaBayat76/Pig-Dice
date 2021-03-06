package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.dto.PlayedGameDTO;
import me.rezabayat.pigdice.dto.UserDTO;
import me.rezabayat.pigdice.dto.UsersDTO;
import me.rezabayat.pigdice.service.playinggame.GamePlayingInfo;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WebSocketSender {
    private final SimpMessagingTemplate messagingTemplate;
    private final ModelMapper modelMapper;
    private final Map<String, UserDTO> onlineUsers = new HashMap<>();


    public WebSocketSender(SimpMessagingTemplate messagingTemplate, ModelMapper modelMapper) {
        this.messagingTemplate = messagingTemplate;
        this.modelMapper = modelMapper;
    }

    public boolean isLogin(Long id) {
        return this.onlineUsers.values().stream().anyMatch(userDTO -> Objects.equals(userDTO.getId(), id));
    }

    public Map.Entry<String, UserDTO> getOnlineUser(Long id) {
        for (Map.Entry<String, UserDTO> entry : onlineUsers.entrySet()) {
            if (Objects.equals(entry.getValue().getId(), id)) {
                return entry;
            }
        }
        return null;
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
            if (!users.isEmpty()) {
                UsersDTO usersDTO = new UsersDTO();
                usersDTO.setUsers(users);
                this.messagingTemplate.convertAndSendToUser(sessionId, "/user/online-users", usersDTO);

            }
        } catch (Exception ignore) {

        }
    }

    public void sendOnlineGames(List<GameEntity> gameEntities) {
        try {
            List<GameDTO> onlineGames = gameEntities.stream().map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class)).collect(Collectors.toList());
            if (!onlineGames.isEmpty()) {
                this.messagingTemplate.convertAndSend("/topic/online-games", onlineGames);
            }
        } catch (Exception ignore) {

        }
    }

    public void notifyUserToStartGame(UserEntity firstPlayer, UserEntity secondPlayer, PlayedGameEntity playedGameEntity) {
        try {
            Map.Entry<String, UserDTO> first = getOnlineUser(firstPlayer.getId());
            Map.Entry<String, UserDTO> second = getOnlineUser(secondPlayer.getId());
            System.out.println(first);
            System.out.println(second);
            if (first != null) {
                if (second != null) {
                    PlayedGameDTO playedGameDTO = this.modelMapper.map(playedGameEntity, PlayedGameDTO.class);
                    System.out.println(playedGameDTO);
                    this.messagingTemplate.convertAndSendToUser(first.getKey(), "/user/game-start", playedGameDTO);
                    this.messagingTemplate.convertAndSendToUser(second.getKey(), "/user/game-start", playedGameDTO);

                } else {
                    this.messagingTemplate.convertAndSendToUser(first.getKey(), "/user/kill", "Competitor disconnected");

                }
            } else if (second != null) {
                this.messagingTemplate.convertAndSendToUser(second.getKey(), "/user/kill", "Competitor disconnected");
            }
        } catch (Exception ignore) {

        }
    }

    public void notifyHold(Long holdPlayer, Long activePlayer) {
        Map.Entry<String, UserDTO> hold = getOnlineUser(holdPlayer);
        Map.Entry<String, UserDTO> active = getOnlineUser(activePlayer);
        if (hold != null) {
            if (active != null) {
                this.messagingTemplate.convertAndSendToUser(hold.getKey(), "/user/hold", false);
                this.messagingTemplate.convertAndSendToUser(active.getKey(), "/user/hold", true);

            } else {
                this.messagingTemplate.convertAndSendToUser(hold.getKey(), "/user/kill", "Competitor disconnected");

            }
        } else if (active != null) {
            this.messagingTemplate.convertAndSendToUser(active.getKey(), "/user/kill", "Competitor disconnected");
        }
    }


    public void notifyGameInfo(Long firstPlayer, Long secondPlayer, GamePlayingInfo gameInfo) {
        try {
            System.out.println("send Game Info");
            Map.Entry<String, UserDTO> firstUser = getOnlineUser(firstPlayer);
            Map.Entry<String, UserDTO> secondUser = getOnlineUser(secondPlayer);
            if (firstUser != null) {
                if (secondUser != null) {
                    this.messagingTemplate.convertAndSendToUser(firstUser.getKey(), "/user/game-info", gameInfo);
                    this.messagingTemplate.convertAndSendToUser(secondUser.getKey(), "/user/game-info", gameInfo);

                } else {
                    this.messagingTemplate.convertAndSendToUser(firstUser.getKey(), "/user/kill", "Competitor disconnected");

                }
            } else if (secondUser != null) {
                this.messagingTemplate.convertAndSendToUser(secondUser.getKey(), "/user/kill", "Competitor disconnected");
            }
        } catch (Exception ignore) {

        }
    }
}
