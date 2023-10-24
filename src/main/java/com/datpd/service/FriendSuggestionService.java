package com.datpd.service;

import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.FriendSuggestionEntity;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.FriendSuggestionMapper;
import com.datpd.repository.FriendSuggestionRepository;
import com.datpd.repository.UserRepository;
import com.datpd.utils.CacheKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendSuggestionService {

    private final UserRepository userRepository;

    private final FriendSuggestionMapper friendSuggestionMapper;

    private final FriendSuggestionRepository friendSuggestionRepository;

    private final FriendService friendService;

    private final RedissonClient redissonClient;

    public FriendSuggestionService(UserRepository userRepository,
                                   FriendSuggestionMapper friendSuggestionMapper,
                                   FriendSuggestionRepository friendSuggestionRepository,
                                   FriendService friendService,
                                   RedissonClient redissonClient) {
        this.userRepository = userRepository;
        this.friendSuggestionMapper = friendSuggestionMapper;
        this.friendSuggestionRepository = friendSuggestionRepository;
        this.friendService = friendService;
        this.redissonClient = redissonClient;
    }


    public Set<String> getFriendSuggestionPhonNumbersByUserId(long userId) {
        log.info("Get all friend suggestion phone numbers by userId: {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()) {
            Set<String> friendSuggestionPhoneNumbers =
                    friendSuggestionRepository.findAllByUserTargetId(userId)
                            .stream()
                            .map(FriendSuggestionEntity::getUserFriendPhoneSuggestion)
                            .collect(Collectors.toSet());
            friendSuggestionPhoneNumbers.remove(userEntity.get().getPrimaryPhoneNumber());
            return friendSuggestionPhoneNumbers;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void makeFriendSuggestions(long userId, List<ContactPhoneNumberEntity> contactPhoneNumberEntities) {
        log.info("Make friend suggestions by userId: {} start", userId);
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isPresent()) {
            List<FriendSuggestionEntity> to = new ArrayList<>();
            UserEntity userEntity = optionalUserEntity.get();
            Set<String> currentFriendPhoneNumbers = friendService.getFriendPhoneNumbersByUserId(userId);
            Set<String> currentFriendSuggestionPhoneNumbers = getFriendSuggestionPhonNumbersByUserId(userId);
            contactPhoneNumberEntities.forEach(contactPhoneNumberEntity -> {
                if (!currentFriendPhoneNumbers.contains(contactPhoneNumberEntity.getContactPhoneNumber())
                        && !currentFriendSuggestionPhoneNumbers.contains(contactPhoneNumberEntity.getContactPhoneNumber())) {
                    UserEntity userFriendSuggestion =
                            userRepository.findByPrimaryPhoneNumber(contactPhoneNumberEntity.getContactPhoneNumber());
                    if (userFriendSuggestion != null) {
                        to.add(FriendSuggestionEntity.builder()
                                .userTargetId(userId)
                                .userTargetName(userEntity.getName())
                                .userPhoneTarget(userEntity.getPrimaryPhoneNumber())
                                .userFriendSuggestionId(userFriendSuggestion.getId())
                                .userFriendNameSuggestion(userFriendSuggestion.getPrimaryPhoneNumber())
                                .userFriendPhoneSuggestion(userFriendSuggestion.getPrimaryPhoneNumber())
                                .build());
                    }
                }
            });

            friendSuggestionRepository.saveAll(to);
        }

    }

    public List<FriendSuggestionDto> getFriendSuggestionsByUserId(long userId) {
        log.info("Get friend suggestions by userId: {}", userId);
        RBucket<List<FriendSuggestionDto>> friendSuggestionDtoListBucket = redissonClient.getBucket(CacheKeyEnum.USER_FRIEND_SUGGESTIONS.genKey(userId));
        List<FriendSuggestionDto> cachedFriendSuggestionDtoList = friendSuggestionDtoListBucket.get();

        if (cachedFriendSuggestionDtoList != null)
            return cachedFriendSuggestionDtoList;

        List<FriendSuggestionEntity> friendSuggestionEntities = friendSuggestionRepository.findAllByUserTargetId(userId);
        List<FriendSuggestionDto> friendSuggestionDtoList =
                friendSuggestionEntities.size() > 0 ? friendSuggestionMapper.map(friendSuggestionEntities) : null;

        if (friendSuggestionDtoList != null) {
            friendSuggestionDtoListBucket.set(friendSuggestionDtoList);
            friendSuggestionDtoListBucket.expire(10, TimeUnit.MINUTES);
        }

        return friendSuggestionDtoList;
    }
}
