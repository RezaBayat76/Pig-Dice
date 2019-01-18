package me.rezabayat.pigdice.service.playinggame;

import lombok.Data;
import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;

@Data
public class GamePlayingService {

    private GamePlayingInfo gameInfo;
    private UserEntity firstPlayer;
    private UserEntity secondPlayer;
    private GameEntity game;
    private PlayedGameEntity playedGameEntity;
}
