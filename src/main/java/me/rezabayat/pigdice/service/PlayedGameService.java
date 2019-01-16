package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameCommentEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.PlayedGameCommentRepository;
import me.rezabayat.pigdice.dal.repository.PlayedGameRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayedGameService {

    private final PlayedGameRepository playedGameRepository;
    private final PlayedGameCommentRepository playedGameCommentRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    public PlayedGameService(PlayedGameRepository playedGameRepository, PlayedGameCommentRepository playedGameCommentRepository, GameRepository gameRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, ModelMapper modelMapper) {
        this.playedGameRepository = playedGameRepository;
        this.playedGameCommentRepository = playedGameCommentRepository;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.modelMapper = modelMapper;
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

    public void addComment(CommentOnPlayedGame commentOnPlayedGame, String token) {
        String usernameCommentCreator = this.jwtTokenUtil.getUsername(token);

        Optional<UserEntity> optionalUserCreator = this.userRepository.findByUsername(usernameCommentCreator);

        if (!optionalUserCreator.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<PlayedGameEntity> optionalPlayedGameEntity = this.playedGameRepository.findById(commentOnPlayedGame.getPlayedGameId());

        if (!optionalPlayedGameEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        PlayedGameCommentEntity playedGameCommentEntity = this.modelMapper.map(commentOnPlayedGame, PlayedGameCommentEntity.class);
        playedGameCommentEntity.setPlayedGame(optionalPlayedGameEntity.get());
        playedGameCommentEntity.setUser(optionalUserCreator.get());
        this.playedGameCommentRepository.save(playedGameCommentEntity);
    }
    
    @Transactional
    public List<PlayedGameCommentDTO> comments(long id) {
        Optional<PlayedGameEntity> optionalPlayedGame = this.playedGameRepository.findById(id);

        if (!optionalPlayedGame.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        return optionalPlayedGame.get().getPlayedGameComments().stream()
                .map(playedGameCommentEntity -> this.modelMapper.map(playedGameCommentEntity, PlayedGameCommentDTO.class))
                .collect(Collectors.toList());
    }
}
