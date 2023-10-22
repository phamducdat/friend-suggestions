package com.datpd.mapper;

import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendSuggestionMapper {

    public FriendSuggestionDto map(ContactPhoneNumberEntity userEntity) {
        return FriendSuggestionDto.builder()
                .friendSuggestionName(userEntity.getContactUserName())
                .friendSuggestionPhoneNumber(userEntity.getContactPhoneNumber())
                .build();
    }

    public List<FriendSuggestionDto> map(List<ContactPhoneNumberEntity> userEntities) {
        return userEntities.stream().map(this::map).collect(Collectors.toList());
    }
}
