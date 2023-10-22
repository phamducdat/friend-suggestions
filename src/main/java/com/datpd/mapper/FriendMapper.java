package com.datpd.mapper;

import com.datpd.dto.FriendDto;
import com.datpd.entity.FriendEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendMapper {

    public FriendDto mapByUserId(long userId, FriendEntity friendEntity) {
        long friendId;
        String friendName;
        String friendPhoneNumber;

        if (friendEntity.getUserId1() != userId) {
            friendId = friendEntity.getUserId1();
            friendName = friendEntity.getUserName1();
            friendPhoneNumber = friendEntity.getPhoneNumber1();
        } else {
            friendId = friendEntity.getUserId2();
            friendName = friendEntity.getUserName2();
            friendPhoneNumber = friendEntity.getPhoneNumber2();
        }

        return FriendDto.builder()
                .friendId(friendId)
                .friendName(friendName)
                .friendPhoneNumber(friendPhoneNumber)
                .build();
    }

    public List<FriendDto> mapByUserId(long userId, List<FriendEntity> friendEntities) {
        return friendEntities.stream().map(friendEntity ->
                mapByUserId(userId, friendEntity)).collect(Collectors.toList());
    }

}
