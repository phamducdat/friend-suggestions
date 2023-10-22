package com.datpd.service;

import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.mapper.FriendSuggestionMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.FriendRepository;
import com.datpd.repository.UserRepository;
import com.datpd.utils.CacheKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendSuggestionService {

    private final UserRepository userRepository;
    private final ContactPhoneNumberRepository contactPhoneNumberRepository;

    private final FriendSuggestionMapper friendSuggestionMapper;

    private final FriendRepository friendRepository;

    private final FriendService friendService;

    private final RedissonClient redissonClient;

    public FriendSuggestionService(UserRepository userRepository,
                                   ContactPhoneNumberRepository contactPhoneNumberRepository,
                                   FriendSuggestionMapper friendSuggestionMapper,
                                   FriendRepository friendRepository, FriendService friendService,
                                   RedissonClient redissonClient) {
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.friendSuggestionMapper = friendSuggestionMapper;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
        this.redissonClient = redissonClient;
    }

    public List<FriendSuggestionDto> getFriendSuggestionsByUserId(long userId) {
        log.info("Get friend suggestions by userId: {}", userId);
        RBucket<List<FriendSuggestionDto>> friendSuggestionDtoListBucket = redissonClient.getBucket(CacheKeyEnum.USER_FRIEND_SUGGESTIONS.genKey(userId));
        List<FriendSuggestionDto> cachedFriendSuggestionDtoList = friendSuggestionDtoListBucket.get();

        if (cachedFriendSuggestionDtoList != null)
            return cachedFriendSuggestionDtoList;

        List<ContactPhoneNumberEntity> contactPhoneNumberEntities =
                contactPhoneNumberRepository.getContactPhoneNumberEntitiesByUserId(userId);

        Set<String> friendPhoneNumbers = friendService.getFriendPhoneNumbersByUserId(userId);

        List<ContactPhoneNumberEntity> friendSuggestionPhoneNumbers =
                contactPhoneNumberEntities.stream().filter(s -> !friendPhoneNumbers.contains(s.getContactPhoneNumber())
                                && userRepository.findByPrimaryPhoneNumber(s.getContactPhoneNumber()) != null)
                        .collect(Collectors.toList());

        List<FriendSuggestionDto> friendSuggestionDtoList = friendSuggestionMapper.map(friendSuggestionPhoneNumbers);

        if (friendSuggestionDtoList != null) {
            friendSuggestionDtoListBucket.set(friendSuggestionDtoList);
            friendSuggestionDtoListBucket.expire(10, TimeUnit.MINUTES);
        }

        return friendSuggestionDtoList;
    }
}
