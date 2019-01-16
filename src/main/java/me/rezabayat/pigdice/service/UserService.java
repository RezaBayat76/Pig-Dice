package me.rezabayat.pigdice.service;

import me.rezabayat.pigdice.dal.entity.UserEntity;
import me.rezabayat.pigdice.dal.repository.UserRepository;
import me.rezabayat.pigdice.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void createNewUser(UserDTO userDTO) {
        UserEntity userEntity = this.modelMapper.map(userDTO, UserEntity.class);
        this.userRepository.save(userEntity);
    }
}
