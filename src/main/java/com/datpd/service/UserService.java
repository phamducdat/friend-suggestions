package com.datpd.service;

import com.datpd.dto.UserDto;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.UserMapper;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDto> getUsers() {
        log.info("Get all users");
        return userMapper.map(userRepository.findAll());
    }

    public UserDto getUserById(long userId) {
        log.info("Get user by id: {}", userId);
        Optional<UserEntity> userEntityOptional =
                userRepository.findById(userId);
        return userEntityOptional.map(userMapper::map).orElse(null);
    }
}
