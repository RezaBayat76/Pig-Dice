package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.LoginDTO;
import me.rezabayat.pigdice.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenGenerator jwtTokenGenerator;
    public UserService(UserRepository userRepository, ModelMapper modelMapper, JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public void createNewUser(UserDTO userDTO) {
        UserEntity userEntity = this.modelMapper.map(userDTO, UserEntity.class);
        this.userRepository.save(userEntity);
    }

    public UserDTO authenticate(LoginDTO loginDTO) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());

        if (optionalUserEntity.isPresent()){
            UserDTO userDTO = modelMapper.map(optionalUserEntity.get(), UserDTO.class);
            userDTO.setToken(this.jwtTokenGenerator.generateToken(userDTO.getUsername()));
            return userDTO;
        }
        throw new AssertionError("username or password incorrect");
    }
}
