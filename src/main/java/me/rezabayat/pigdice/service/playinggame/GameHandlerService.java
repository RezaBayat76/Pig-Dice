package me.rezabayat.pigdice.service.playinggame;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.PlayedGameRepository;
import me.rezabayat.pigdice.service.WebSocketSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
            GamePlayingInfo gamePlayingInfo = new GamePlayingInfo();
            UserGameInfo firstUserGameInfo = new UserGameInfo(true);
            UserGameInfo secondUserGameInfo = new UserGameInfo(false);
            gamePlayingInfo.getPlayersGameInfo().put(pendingGame.getFirstPlayer().getId(), firstUserGameInfo);
            gamePlayingInfo.getPlayersGameInfo().put(pendingGame.getSecondPlayer().getId(), secondUserGameInfo);

            pendingGame.setGameInfo(gamePlayingInfo);

            this.webSocketSender.notifyUserToStartGame(pendingGame.getFirstPlayer(), pendingGame.getSecondPlayer(), playedGameEntity);
            this.webSocketSender.notifyGameInfo(pendingGame.getFirstPlayer().getId(), pendingGame.getSecondPlayer().getId(), pendingGame.getGameInfo());
            this.webSocketSender.sendOnlineGames(this.onlineGames.values().stream().map(GamePlayingService::getGame).collect(Collectors.toList()));

        } else {
            GamePlayingService gamePlayingService = new GamePlayingService();
            gamePlayingService.setFirstPlayer(userEntity);
            gamePlayingService.setGame(gameEntity);
            pendingGames.put(gameEntity.getId(), gamePlayingService);
            this.webSocketSender.sendOnlineGames(this.pendingGames.values().stream().map(GamePlayingService::getGame).collect(Collectors.toList()));
        }
    }

    public void hold(Long id, UserEntity userEntity) {
        GamePlayingService gamePlayingService = this.onlineGames.get(id);
        gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setHold(true));
        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setHold(false);

        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).calculateScore();
        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setCurrentThrow(0);
        gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setCurrentDices(new ArrayList<>()));

        if (gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).getScore() >= gamePlayingService.getGame().getMaxScore()) {
            gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setWin(true);
            Optional<PlayedGameEntity> playedGameEntity = this.playedGameRepository.findById(id);
            if (playedGameEntity.isPresent()) {
                PlayedGameEntity entity = playedGameEntity.get();
                entity.setWinner(userEntity);
                this.playedGameRepository.save(entity);
            }
        }

        gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setHold(true));
        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setHold(false);

        this.webSocketSender.notifyGameInfo(gamePlayingService.getFirstPlayer().getId(), gamePlayingService.getSecondPlayer().getId(), gamePlayingService.getGameInfo());
    }

    @Transactional
    public void rollDice(UserEntity userEntity, long id) {

        GamePlayingService gamePlayingService = this.onlineGames.get(id);

        List<Long> rollDices = new ArrayList<>();

        for (int i = 0; i < gamePlayingService.getGame().getNumDice(); i++) {
            long rollDice = (long) (Math.random() * 6 + 1);
            rollDices.add(rollDice);
            gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).addCurrentScore(rollDice);

        }
        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).increaseThrow();


        for (Long roll : rollDices) {
            if (Objects.equals(roll, gamePlayingService.getGame().getFallDice())) {
                gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setHold(true));
                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setHold(false);
                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setScore(0);
                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setCurrentScore(0);

            }
        }

        if (gamePlayingService.getGame().getMaxThrow() != null) {
            if (gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).getCurrentThrow() >= gamePlayingService.getGame().getMaxThrow()) {
                gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setHold(true));
                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setHold(false);

                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).calculateScore();
                gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setCurrentThrow(0);

                if (gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).getScore() >= gamePlayingService.getGame().getMaxScore()) {
                    gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setWin(true);
                    Optional<PlayedGameEntity> playedGameEntity = this.playedGameRepository.findById(id);
                    if (playedGameEntity.isPresent()) {
                        PlayedGameEntity entity = playedGameEntity.get();
                        entity.setWinner(userEntity);
                        this.playedGameRepository.save(entity);
                    }
                }
            }
        }

        gamePlayingService.getGameInfo().getPlayersGameInfo().forEach((aLong, userGameInfo) -> userGameInfo.setCurrentDices(new ArrayList<>()));
        gamePlayingService.getGameInfo().getPlayersGameInfo().get(userEntity.getId()).setCurrentDices(rollDices);

        this.webSocketSender.notifyGameInfo(gamePlayingService.getFirstPlayer().getId(), gamePlayingService.getSecondPlayer().getId(), gamePlayingService.getGameInfo());
    }
}
