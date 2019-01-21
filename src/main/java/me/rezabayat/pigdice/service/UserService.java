package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.FollowingEntity;
import me.rezabayat.pigdice.dal.entity.GameEntity;
import me.rezabayat.pigdice.dal.entity.UserCommentEntity;
import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.FollowingRepository;
import me.rezabayat.pigdice.dal.repository.GameRepository;
import me.rezabayat.pigdice.dal.repository.UserCommentRepository;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.*;
import me.rezabayat.pigdice.enums.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserCommentRepository userCommentRepository;
    private final FollowingRepository followingRepository;
    private final GameRepository gameRepository;
    private final WebSocketSender webSocketSender;
    private final ModelMapper modelMapper;
    private final JwtTokenUtil jwtTokenUtil;

    public UserService(UserRepository userRepository, UserCommentRepository userCommentRepository, FollowingRepository followingRepository, GameRepository gameRepository, WebSocketSender webSocketSender, ModelMapper modelMapper, JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.userCommentRepository = userCommentRepository;
        this.followingRepository = followingRepository;
        this.gameRepository = gameRepository;
        this.webSocketSender = webSocketSender;
        this.modelMapper = modelMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public void createNewUser(UserDTO userDTO) {
        if (this.userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("username is exist");
        }
        UserEntity userEntity = this.modelMapper.map(userDTO, UserEntity.class);
        userEntity.setRole(Role.USER);
        this.webSocketSender.sendNewUserToAll(this.modelMapper.map(this.userRepository.save(userEntity), UserDTO.class));
    }

    public UserDTO authenticate(LoginDTO loginDTO) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());

        if (optionalUserEntity.isPresent()) {
            if (this.webSocketSender.isLogin(optionalUserEntity.get().getId())) {
                throw new AssertionError("Already logged in");
            }
            UserDTO userDTO = modelMapper.map(optionalUserEntity.get(), UserDTO.class);
            userDTO.setToken(this.jwtTokenUtil.generateToken(userDTO.getUsername()));
            return userDTO;
        }
        throw new AssertionError("username or password incorrect");
    }

    public List<UserDTO> allUsers() {
        return this.userRepository.findAll().stream()
                .map(userEntity -> this.modelMapper.map(userEntity, UserDTO.class))
                .collect(Collectors.toList());

    }

    public List<UserCommentDTO> commentOnUser(long id) {

        Optional<UserEntity> optionalUserEntity = this.userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        return this.userCommentRepository.findAllByMentionUser(optionalUserEntity.get()).stream().
                map(userCommentEntity -> this.modelMapper.map(userCommentEntity, UserCommentDTO.class))
                .collect(Collectors.toList());
    }

    public void addComment(CommentOnUserDTO commentOnUserDTO, String token) {
        Optional<UserEntity> optionalUserMention = this.userRepository.findById(commentOnUserDTO.getMentionUserID());
        if (!optionalUserMention.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<UserEntity> optionalUserCommentCreator = this.userRepository.findByUsername(jwtTokenUtil.getUsername(token));

        if (!optionalUserCommentCreator.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        UserCommentEntity userCommentEntity = this.modelMapper.map(commentOnUserDTO, UserCommentEntity.class);
        userCommentEntity.setMentionUser(optionalUserMention.get());
        userCommentEntity.setUserCreator(optionalUserCommentCreator.get());

        this.userCommentRepository.save(userCommentEntity);
    }

    public void follow(long id, String token) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(jwtTokenUtil.getUsername(token));

        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        FollowingEntity followingEntity = new FollowingEntity();
        followingEntity.setUser(optionalUserEntity.get());
        followingEntity.setFollowedUser(optionalUser.get());

        this.followingRepository.save(followingEntity);
    }

    @Transactional
    public List<Long> followings(String token) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(jwtTokenUtil.getUsername(token));

        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        return optionalUserEntity.get().getFollowings().stream().map(followingEntity -> followingEntity.getFollowedUser().getId()).collect(Collectors.toList());
    }

    public void unFollow(long id, String token) {
        Optional<UserEntity> optionalUser = this.userRepository.findById(id);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(jwtTokenUtil.getUsername(token));

        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        this.followingRepository.deleteByUserAndFollowedUser(optionalUserEntity.get(), optionalUser.get());
    }

    public UserProfileDTO profile(String token) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsername(jwtTokenUtil.getUsername(token));

        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        UserProfileDTO userProfileDTO = this.modelMapper.map(optionalUserEntity.get(), UserProfileDTO.class);
        userProfileDTO.setFollowings(optionalUserEntity.get().getFollowings().stream().map(followingEntity -> this.modelMapper.map(followingEntity.getFollowedUser(), UserDTO.class)).collect(Collectors.toList()));
        userProfileDTO.setFollowers(optionalUserEntity.get().getFollowers().stream().map(followingEntity -> this.modelMapper.map(followingEntity.getUser(), UserDTO.class)).collect(Collectors.toList()));

        return userProfileDTO;
    }

    public void editProfile(UserDTO userDTO) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findById(userDTO.getId());

        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }

        UserEntity userEntity = optionalUserEntity.get();

        this.modelMapper.map(userDTO, userEntity);

        this.userRepository.save(userEntity);
    }

    public List<UserCommentDTO> uncheckedComments() {
        List<UserCommentEntity> userCommentEntities = this.userCommentRepository.uncheckedComments();
        return userCommentEntities.stream().map(userCommentEntity -> this.modelMapper.map(userCommentEntity, UserCommentDTO.class)).collect(Collectors.toList());
    }

    public void acceptComments(long id) {
        Optional<UserCommentEntity> optional = this.userCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }
        optional.get().setAccepted(true);
        this.userCommentRepository.save(optional.get());
    }

    public void declineComment(long id) {
        Optional<UserCommentEntity> optional = this.userCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }
        optional.get().setAccepted(false);
        this.userCommentRepository.save(optional.get());
    }

    public List<GameDTO> designedGames(Long id) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new IllegalArgumentException("Illegal request");
        }
        List<GameEntity> gameEntities = this.gameRepository.designedGames(optionalUserEntity.get());
        return gameEntities.stream().map(gameEntity -> this.modelMapper.map(gameEntity, GameDTO.class)).collect(Collectors.toList());
    }
}
