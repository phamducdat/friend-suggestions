package com.datpd.service;

import com.datpd.dto.UserDto;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.UserMapper;
import com.datpd.repository.UserRepository;
import com.datpd.utils.CacheKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final RedissonClient redissonClient;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       RedissonClient redissonClient,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.redissonClient = redissonClient;
        this.userMapper = userMapper;
    }

    public Page<UserDto> getUsers(int page, int pageSize) {
        log.info("Get all users with page: {} and page size : {}", page, pageSize);
        Page<UserEntity> userEntityPage = userRepository.findAll(PageRequest.of(page, pageSize));
        return userEntityPage.map(userMapper::map);
    }


    public UserDto getUserById(long userId) {
        log.info("Get user by id: {}", userId);
        RBucket<UserDto> userDtoRBucket = redissonClient.getBucket(CacheKeyEnum.USER_DTO.genKey(userId));
        UserDto cachedUserDto = userDtoRBucket.get();

        if (cachedUserDto != null)
            return cachedUserDto;

        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        UserDto userDto = userEntityOptional.map(userMapper::map).orElse(null);

        if (userDto != null) {
            userDtoRBucket.set(userDto);
            userDtoRBucket.expire(10, TimeUnit.MINUTES);
        }

        return userDto;
    }
}
