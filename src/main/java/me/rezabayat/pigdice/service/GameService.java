package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameCommentEntity;
import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.PlayedGameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.GameCommentRepository;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.PlayedGameRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.CommentOnGameDTO;
import me.rezabayat.pigdice.dto.GameCommentDTO;
import me.rezabayat.pigdice.dto.GameDTO;
import me.rezabayat.pigdice.dto.UpdatedScoreDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameCommentRepository gameCommentRepository;
    private final PlayedGameRepository playedGameRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    public GameService(GameRepository gameRepository, UserRepository userRepository, GameCommentRepository gameCommentRepository, PlayedGameRepository playedGameRepository, JwtTokenUtil jwtTokenUtil, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.gameCommentRepository = gameCommentRepository;
        this.playedGameRepository = playedGameRepository;
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
        gameEntity.setCreateDate(new Date());

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

        return optionalGames.get().getGameComments().stream().filter(GameCommentEntity::getAccepted)
                .map(gameCommentEntity -> this.modelMapper.map(gameCommentEntity, GameCommentDTO.class))
                .collect(Collectors.toList());
    }

    public GameDTO getGame(long id) {
        Optional<GameEntity> optionalGame = this.gameRepository.findById(id);

        if (!optionalGame.isPresent()) {
            throw new IllegalArgumentException("Game not found");
        }

        return this.modelMapper.map(optionalGame.get(), GameDTO.class);
    }

    public List<GameDTO> bestGames() {
        List<GameEntity> gameEntities = this.gameRepository.bestGames();
        return gameEntities.stream().map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class)).limit(3).collect(Collectors.toList());
    }

    public List<GameDTO> mostPlaying() {
        List<GameEntity> gameEntities = this.gameRepository.mostPlaying();
        return gameEntities.stream().map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class)).limit(3).collect(Collectors.toList());
    }

    public List<GameDTO> bestRecently() {
        List<GameEntity> gameEntities = this.gameRepository.bestRecently();
        return gameEntities.stream().map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class)).limit(3).collect(Collectors.toList());
    }

    @Transactional
    public void updateScore(UpdatedScoreDTO updatedScoreDTO) {
        Optional<PlayedGameEntity> playedGameEntityOptional = this.playedGameRepository.findById(updatedScoreDTO.getPlayedGameId());
        if (!playedGameEntityOptional.isPresent()) {
            throw new IllegalArgumentException("Game not found");
        }
        Optional<GameEntity> optionalGame = this.gameRepository.findById(updatedScoreDTO.getGameId());

        if (!optionalGame.isPresent()) {
            throw new IllegalArgumentException("Game not found");
        }

        PlayedGameEntity playedGameEntity = playedGameEntityOptional.get();
        GameEntity gameEntity = optionalGame.get();

        if (playedGameEntity.getScore() == null)
            playedGameEntity.setScore(updatedScoreDTO.getScore());
        else
            playedGameEntity.setScore(playedGameEntity.getScore() + updatedScoreDTO.getScore());

        if (gameEntity.getAverageScore() == null) {
            gameEntity.setAverageScore(playedGameEntity.getScore());
            gameEntity.setMaxScore(playedGameEntity.getScore());

        } else {
            long avgScore = (gameEntity.getAverageScore() * (gameEntity.getNumPlayed() - 1) + playedGameEntity.getScore()) / gameEntity.getNumPlayed();
            gameEntity.setAverageScore(avgScore);

            if (gameEntity.getMaxScore() < playedGameEntity.getScore()) {
                gameEntity.setMaxScore(playedGameEntity.getScore());
            }
        }

        this.gameRepository.save(gameEntity);
        this.playedGameRepository.save(playedGameEntity);

    }

    public List<GameCommentDTO> uncheckedComments() {
        List<GameCommentEntity> allUncheckedComments = this.gameCommentRepository.findAllUncheckedComments();
        return allUncheckedComments.stream().map(gameCommentEntity -> this.modelMapper.map(gameCommentEntity, GameCommentDTO.class)).collect(Collectors.toList());
    }

    public void acceptComments(long id) {
        Optional<GameCommentEntity> optional = this.gameCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }
        optional.get().setAccepted(true);
        this.gameCommentRepository.save(optional.get());
    }

    public void declineComment(long id) {
        Optional<GameCommentEntity> optional = this.gameCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }
        optional.get().setAccepted(false);
        this.gameCommentRepository.save(optional.get());
    }
}
