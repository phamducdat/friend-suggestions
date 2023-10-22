package com.datpd.mapper;

import com.datpd.dto.UserDto;
import com.datpd.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public UserDto map(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .primaryPhoneNumber(userEntity.getPrimaryPhoneNumber())
                .build();
    }

    public List<UserDto> map(List<UserEntity> userEntities) {
        return userEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
