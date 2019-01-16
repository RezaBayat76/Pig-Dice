package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.GameDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ModelMapper modelMapper;

    public GameService(GameRepository gameRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil, ModelMapper modelMapper) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
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
}
