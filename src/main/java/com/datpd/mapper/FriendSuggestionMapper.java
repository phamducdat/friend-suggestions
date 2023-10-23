package com.datpd.mapper;

import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.FriendSuggestionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendSuggestionMapper {


    public FriendSuggestionDto map(FriendSuggestionEntity friendSuggestionEntity) {
        return FriendSuggestionDto.builder()
                .friendSuggestionName(friendSuggestionEntity.getUserFriendNameSuggestion())
                .friendSuggestionPhoneNumber(friendSuggestionEntity.getUserFriendPhoneSuggestion())
                .build();
    }

    public List<FriendSuggestionDto> map(List<FriendSuggestionEntity> friendSuggestionEntities) {
        return friendSuggestionEntities.stream().map(this::map).collect(Collectors.toList());
    }

}
