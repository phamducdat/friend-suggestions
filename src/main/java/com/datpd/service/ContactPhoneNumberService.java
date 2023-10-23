package com.datpd.service;

import com.datpd.domain.ContactPhoneNumbersDomain;
import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.dto.FriendSuggestionDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.mapper.ContactPhoneNumberMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.utils.CacheKeyEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ContactPhoneNumberService {

    private final ContactPhoneNumberRepository contactPhoneNumberRepository;
    private final ContactPhoneNumberMapper contactPhoneNumberMapper;
    private final FriendService friendService;
    private final FriendSuggestionService friendSuggestionService;
    private final RedissonClient redissonClient;

    private final ObjectMapper objectMapper;

    public ContactPhoneNumberService(ContactPhoneNumberRepository contactPhoneNumberRepository,
                                     ContactPhoneNumberMapper contactPhoneNumberMapper,
                                     FriendService friendService,
                                     FriendSuggestionService friendSuggestionService,
                                     RedissonClient redissonClient, ObjectMapper objectMapper) {
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.contactPhoneNumberMapper = contactPhoneNumberMapper;
        this.friendService = friendService;
        this.friendSuggestionService = friendSuggestionService;
        this.redissonClient = redissonClient;
        this.objectMapper = objectMapper;
    }

    public List<ContactPhoneNumberDto> getAllContactPhoneNumberByUserId(long userId) {
        List<ContactPhoneNumberEntity> contactPhoneNumberEntities = contactPhoneNumberRepository.getContactPhoneNumberEntitiesByUserId(userId);
        if (contactPhoneNumberEntities.size() > 0) {
            log.info("Get all contact phone numbers by userId: {}", userId);
            return contactPhoneNumberMapper.map(contactPhoneNumberEntities);
        }
        return null;
    }
    @Transactional(rollbackFor = Exception.class)
    public void processContactPhoneNumbers(ConsumerRecord<Long, String> consumerRecord)
            throws JsonProcessingException {
        ContactPhoneNumbersDomain contactPhoneNumbersDomain =
                objectMapper.readValue(consumerRecord.value(), ContactPhoneNumbersDomain.class);

        log.info("processContactPhoneNumbers: {}", contactPhoneNumbersDomain);

        switch (contactPhoneNumbersDomain.getApiType()) {
            case UPDATE:
                updateContactPhoneNumbersByUserId(contactPhoneNumbersDomain.getUserId(),
                        contactPhoneNumbersDomain.getContactPhoneNumberDtoList());
                break;
            default:
                log.info("Invalid API type of contactPhoneNumbersDomain: {}", contactPhoneNumbersDomain.getApiType());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContactPhoneNumbersByUserId(long userId,
                                                  List<ContactPhoneNumberDto> contactPhoneNumberDtoList) {
        log.info("Update contact phone numbers by userId: {}", userId);
        RBucket<List<FriendSuggestionDto>> friendSuggestionDtoListBucket = redissonClient.getBucket(CacheKeyEnum.USER_FRIEND_SUGGESTIONS.genKey(userId));
        if (contactPhoneNumberRepository.findAllByUserId(userId).size() > 0)
            contactPhoneNumberRepository.deleteAllByUserId(userId);
        List<ContactPhoneNumberEntity> contactPhoneNumberEntities = contactPhoneNumberRepository.saveAll(contactPhoneNumberMapper.map(userId, contactPhoneNumberDtoList));
        friendSuggestionDtoListBucket.delete();
        friendService.makeFriends(userId, contactPhoneNumberEntities);
        friendSuggestionService.makeFriendSuggestions(userId, contactPhoneNumberEntities);
    }

}
