package com.datpd.service;

import com.datpd.dto.FriendDto;
import com.datpd.entity.FriendEntity;
import com.datpd.mapper.FriendMapper;
import com.datpd.repository.FriendRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;

    private final FriendMapper friendMapper;


    public FriendService(FriendRepository friendRepository,
                         FriendMapper friendMapper) {
        this.friendRepository = friendRepository;
        this.friendMapper = friendMapper;
    }

    public List<FriendDto> getAllFriendsByUserId(long userId) {
        log.info("Get all friends by userId: {}", userId);
        List<FriendEntity> friendEntities = friendRepository.findAllByUserId(userId);
        if (friendEntities.size() > 0)
            return friendMapper.mapByUserId(userId, friendEntities);
        return null;
    }


}
