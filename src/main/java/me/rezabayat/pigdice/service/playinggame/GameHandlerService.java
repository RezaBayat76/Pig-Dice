package me.rezabayat.pigdice.service.playinggame;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.PlayedGameRepository;
import me.rezabayat.pigdice.service.WebSocketSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GameHandlerService {
    private final WebSocketSender webSocketSender;
    private final PlayedGameRepository playedGameRepository;
    private Map<Long, GamePlayingService> pendingGames = new HashMap<>();
    private Map<Long, GamePlayingService> onlineGames = new HashMap<>();

    public GameHandlerService(WebSocketSender webSocketSender, PlayedGameRepository playedGameRepository) {
        this.webSocketSender = webSocketSender;
        this.playedGameRepository = playedGameRepository;
    }

    public PlayedGameEntity addGame(GameEntity gameEntity, UserEntity firstPlayer, UserEntity secondPlayer) {

        PlayedGameEntity playedGameEntity = new PlayedGameEntity();
        playedGameEntity.setGame(gameEntity);
        playedGameEntity.setFirstPlayer(firstPlayer);
        playedGameEntity.setSecondPlayer(secondPlayer);
        playedGameEntity.setCreateDate(new Date());

        this.playedGameRepository.save(playedGameEntity);

        return playedGameEntity;
    }

    public void play(UserEntity userEntity, GameEntity gameEntity) {
        if (pendingGames.containsKey(gameEntity.getId())) {
            GamePlayingService pendingGame = pendingGames.remove(gameEntity.getId());
            pendingGame.setSecondPlayer(userEntity);

            PlayedGameEntity playedGameEntity = addGame(gameEntity, pendingGame.getFirstPlayer(), pendingGame.getSecondPlayer());
            pendingGame.setPlayedGameEntity(playedGameEntity);
            onlineGames.put(playedGameEntity.getId(), pendingGame);
            this.webSocketSender.notifyUserToStartGame(pendingGame.getFirstPlayer(), pendingGame.getSecondPlayer(), playedGameEntity);
            this.webSocketSender.sendOnlineGames(this.onlineGames.values().stream().map(GamePlayingService::getGame).collect(Collectors.toList()));

        } else {
            GamePlayingService gamePlayingService = new GamePlayingService();
            gamePlayingService.setFirstPlayer(userEntity);
            gamePlayingService.setGame(gameEntity);
            pendingGames.put(gameEntity.getId(), gamePlayingService);
            this.webSocketSender.sendOnlineGames(this.pendingGames.values().stream().map(GamePlayingService::getGame).collect(Collectors.toList()));
        }
    }

    public void hold(PlayedGameEntity playedGameEntity, UserEntity userEntity) {
        if (userEntity.getId() == playedGameEntity.getFirstPlayer().getId()) {
            this.webSocketSender.notifyHold(userEntity.getId(), playedGameEntity.getSecondPlayer().getId());
        } else {
            this.webSocketSender.notifyHold(playedGameEntity.getSecondPlayer().getId(), userEntity.getId());

        }
    }
}
