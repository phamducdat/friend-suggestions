package com.datpd.service;

import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.UserEntity;
import com.datpd.mapper.FriendSuggestionMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.FriendRepository;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendSuggestionService {

    private final UserRepository userRepository;
    private final ContactPhoneNumberRepository contactPhoneNumberRepository;

    private final FriendSuggestionMapper friendSuggestionMapper;

    private final FriendRepository friendRepository;

    private final FriendService friendService;

    public FriendSuggestionService(UserRepository userRepository,
                                   ContactPhoneNumberRepository contactPhoneNumberRepository,
                                   FriendSuggestionMapper friendSuggestionMapper,
                                   FriendRepository friendRepository, FriendService friendService) {
        this.userRepository = userRepository;
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.friendSuggestionMapper = friendSuggestionMapper;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
    }

    public List<FriendSuggestionDto> getFriendSuggestionsByUserId(long userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            log.info("Get friend suggestions by userId: {}", userId);
            List<ContactPhoneNumberEntity> contactPhoneNumberEntities =
                    contactPhoneNumberRepository.getContactPhoneNumberEntitiesByUserId(userId);

            Set<String> friendPhoneNumbers = friendService.getFriendPhoneNumbersByUserId(userId);

            List<ContactPhoneNumberEntity> friendSuggestionPhoneNumbers =
                    contactPhoneNumberEntities.stream().filter(s -> !friendPhoneNumbers.contains(s.getContactPhoneNumber())
                                    && userRepository.findByPrimaryPhoneNumber(s.getContactPhoneNumber()) != null)
                            .collect(Collectors.toList());

            return friendSuggestionMapper.map(friendSuggestionPhoneNumbers);
        }
        return null;
    }
}
