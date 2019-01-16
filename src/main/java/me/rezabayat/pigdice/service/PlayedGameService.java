package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.PlayedGameRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PlayedGameService {

    private final PlayedGameRepository playedGameRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    public PlayedGameService(PlayedGameRepository playedGameRepository, GameRepository gameRepository, UserRepository userRepository) {
        this.playedGameRepository = playedGameRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    public PlayedGameEntity addGame(GameDTO gameDTO, UserDTO firstPlayer, UserDTO secondPlayer) {

        Optional<GameEntity> optionalGameEntity = this.gameRepository.findById(gameDTO.getId());
        if (!optionalGameEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<UserEntity> optionalFirstPlayer = this.userRepository.findById(firstPlayer.getId());
        if (!optionalFirstPlayer.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<UserEntity> optionalSecondPlayer = this.userRepository.findById(secondPlayer.getId());
        if (!optionalSecondPlayer.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        PlayedGameEntity playedGameEntity = new PlayedGameEntity();
        playedGameEntity.setGame(optionalGameEntity.get());
        playedGameEntity.setFirstPlayer(optionalFirstPlayer.get());
        playedGameEntity.setSecondPlayer(optionalSecondPlayer.get());
        playedGameEntity.setCreateDate(new Date());

        this.playedGameRepository.save(playedGameEntity);

        return playedGameEntity;
    }
}
