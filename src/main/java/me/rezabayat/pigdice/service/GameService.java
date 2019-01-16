package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameCommentEntity;
import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.GameCommentRepository;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.CommentOnGameDTO;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import me.rezabayat.pigdice.dto.GameDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameCommentRepository gameCommentRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    public GameService(GameRepository gameRepository, UserRepository userRepository, GameCommentRepository gameCommentRepository, JwtTokenUtil jwtTokenUtil, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameCommentRepository = gameCommentRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.modelMapper = modelMapper;
    }

    public List<GameDTO> games() {
        return this.gameRepository.findAll().stream()
                .map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class))
                .collect(Collectors.toList());
    }

    public void addGame(GameDTO gameDTO, String token) {
        String usernameCreator = this.jwtTokenUtil.getUsername(token);

        Optional<UserEntity> optionalUserCreator = this.userRepository.findByUsername(usernameCreator);

        if (!optionalUserCreator.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        GameEntity gameEntity = this.modelMapper.map(gameDTO, GameEntity.class);

        gameEntity.setUserCreator(optionalUserCreator.get());
        this.gameRepository.save(gameEntity);
    }

    public void addComment(CommentOnGameDTO commentOnGameDTO, String token) {
        String usernameCommentCreator = this.jwtTokenUtil.getUsername(token);

        Optional<UserEntity> optionalUserCreator = this.userRepository.findByUsername(usernameCommentCreator);

        if (!optionalUserCreator.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<GameEntity> optionalGameEntity = this.gameRepository.findById(commentOnGameDTO.getGameId());

        if (!optionalGameEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        GameCommentEntity gameCommentEntity = this.modelMapper.map(commentOnGameDTO, GameCommentEntity.class);
        gameCommentEntity.setGame(optionalGameEntity.get());
        gameCommentEntity.setUser(optionalUserCreator.get());
        this.gameCommentRepository.save(gameCommentEntity);

    }

    @Transactional
    public List<GameCommentDTO> comments(long id) {
        Optional<GameEntity> optionalGames = this.gameRepository.findById(id);

        if (!optionalGames.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        return optionalGames.get().getGameComments().stream()
                .map(gameCommentEntity -> this.modelMapper.map(gameCommentEntity, GameCommentDTO.class))
                .collect(Collectors.toList());
    }
}
