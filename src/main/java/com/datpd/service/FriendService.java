package com.datpd.service;

import com.datpd.dto.FriendDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.FriendEntity;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.FriendMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.FriendRepository;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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


    public FriendService(FriendRepository friendRepository,
                         FriendMapper friendMapper, UserRepository userRepository, ContactPhoneNumberRepository contactPhoneNumberRepository) {
        this.friendRepository = friendRepository;
        this.friendMapper = friendMapper;
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
    }

    public List<FriendDto> getAllFriendsByUserId(long userId) {
        log.info("Get all friends by userId: {}", userId);
        List<FriendEntity> friendEntities = friendRepository.findAllByUserId(userId);
        if (friendEntities.size() > 0)
            return friendMapper.mapByUserId(userId, friendEntities);
        return null;
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

    public void makeFriends(long userId, List<ContactPhoneNumberEntity> contactPhoneNumberEntities) {
        log.info("Make Friends by userId: {}", userId);
        Optional<UserEntity> optionalUserEntity1 = userRepository.findById(userId);
        if (optionalUserEntity1.isPresent()) {
            UserEntity userEntity1 = optionalUserEntity1.get();
            Set<String> currentFriendPhoneNumbers = getFriendPhoneNumbersByUserId(userId);
            contactPhoneNumberEntities.forEach(contactPhoneNumberEntity -> {
                if (!currentFriendPhoneNumbers.contains(contactPhoneNumberEntity.getContactPhoneNumber())) {
                    UserEntity userEntity2 =
                            userRepository.findByPrimaryPhoneNumber(contactPhoneNumberEntity.getContactPhoneNumber());

                    if (userEntity2 != null &&
                            contactPhoneNumberRepository.findByUserIdAndContactPhoneNumber(userEntity2.getId(),
                                    userEntity1.getPrimaryPhoneNumber()) != null)
                        friendRepository.save(friendMapper.map(userEntity1, userEntity2));
                }
            });
        }
    }

}
