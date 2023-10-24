package com.datpd.service;

import com.datpd.dto.FriendDto;
import com.datpd.entity.FriendEntity;
import com.datpd.entity.FriendSuggestionEntity;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.FriendMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.FriendRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FriendService {

    private final FriendRepository friendRepository;

    private final FriendMapper friendMapper;

    private final UserRepository userRepository;

    private final ContactPhoneNumberRepository contactPhoneNumberRepository;

    private final RedissonClient redissonClient;

    private final FriendSuggestionRepository friendSuggestionRepository;


    public FriendService(FriendRepository friendRepository,
                         FriendMapper friendMapper, UserRepository userRepository,
                         ContactPhoneNumberRepository contactPhoneNumberRepository,
                         RedissonClient redissonClient, FriendSuggestionRepository friendSuggestionRepository) {
        this.friendRepository = friendRepository;
        this.friendMapper = friendMapper;
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.redissonClient = redissonClient;
        this.friendSuggestionRepository = friendSuggestionRepository;
    }

    public List<FriendDto> getAllFriendsByUserId(long userId) {
        log.info("Get all friends by userId: {}", userId);

        List<FriendEntity> friendEntities = friendRepository.findAllByUserId(userId);

        return friendMapper.mapByUserId(userId, friendEntities);
    }

    public Set<String> getFriendPhoneNumbersByUserId(long userId) {
        log.info("Get all friend phone numbers by userId: {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()) {
            Set<String> friendPhoneNumbers =
                    friendRepository.findAllByUserId(userId).stream()
                            .flatMap(friendEntity -> Stream.of(friendEntity.getPhoneNumber1(),
                                    friendEntity.getPhoneNumber2()))
                            .collect(Collectors.toSet());
            friendPhoneNumbers.remove(userEntity.get().getPrimaryPhoneNumber());
            return friendPhoneNumbers;
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void makeFriendsAndFriendSuggestions(long userId) {
        Optional<UserEntity> optionalUserEntity1 = userRepository.findById(userId);
        if (optionalUserEntity1.isPresent()) {
            UserEntity userEntity1 = optionalUserEntity1.get();
            List<UserEntity> userEntitiesInNotFriend = userRepository.findUserEntitiesNotInFriendForUserId(userId);
            List<Long> userIdInsNotFriend = userEntitiesInNotFriend.stream().map(UserEntity::getId).collect(Collectors.toList());
            log.info("userEntitiesInNotFriend: {}", userIdInsNotFriend);
            List<Long> newFriendUserIds = contactPhoneNumberRepository.findUserIdsByPhoneNumberAndUserPhoneNumberIds(
                    userEntity1.getPrimaryPhoneNumber(), userIdInsNotFriend);
            log.info("newFriendUserIds : {}", newFriendUserIds);

            List<FriendEntity> friendEntities = new ArrayList<>();
            List<FriendSuggestionEntity> friendSuggestionEntities = new ArrayList<>();
            userEntitiesInNotFriend.forEach(userEntity2 -> {
                if (newFriendUserIds.contains(userEntity2.getId()))
                    friendEntities.add(friendMapper.map(userEntity1, userEntity2));
                else
                    friendSuggestionEntities.add(FriendSuggestionEntity.builder()
                            .userTargetId(userId)
                            .userTargetName(userEntity1.getName())
                            .userPhoneTarget(userEntity1.getPrimaryPhoneNumber())
                            .userFriendSuggestionId(userEntity2.getId())
                            .userFriendNameSuggestion(userEntity2.getPrimaryPhoneNumber())
                            .userFriendPhoneSuggestion(userEntity2.getPrimaryPhoneNumber())
                            .build());
            });
            friendRepository.saveAll(friendEntities);
            log.info("Make Friends by userId: {}", userId);

            friendSuggestionRepository.saveAll(friendSuggestionEntities);
            log.info("Make friend suggestions by userId: {}", userId);

//            friendRepository.saveAll(newFriendUserIds.stream().map(newFriendUserId -> {
//                Optional<UserEntity> optionalUserEntity2 = userEntitiesInNotFriend.stream().filter(userEntity -> userEntity.getId() == newFriendUserId).findFirst();
//                return optionalUserEntity2.map(userEntity -> friendMapper.map(userEntity1, userEntity)).orElse(null);
//            }).collect(Collectors.toList()));

        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void resetFriends(long userId) {
        RBucket<List<FriendDto>> friendsDtoBucket = redissonClient.getBucket(CacheKeyEnum.USER_FRIENDS.genKey(userId));
        friendRepository.deleteAllByUserId1(userId);
        friendRepository.deleteAllByUserId2(userId);
        friendsDtoBucket.delete();
    }

}
