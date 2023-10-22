package com.datpd.service;

import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.mapper.ContactPhoneNumberMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import com.datpd.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ContactPhoneNumberService {

    private final ContactPhoneNumberRepository contactPhoneNumberRepository;
    private final ContactPhoneNumberMapper contactPhoneNumberMapper;
    private final FriendService friendService;

    public ContactPhoneNumberService(ContactPhoneNumberRepository contactPhoneNumberRepository,
                                     ContactPhoneNumberMapper contactPhoneNumberMapper,
                                     FriendService friendService) {
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.contactPhoneNumberMapper = contactPhoneNumberMapper;
        this.friendService = friendService;
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
    public void updateContactPhoneNumbersByUserId(long userId,
                                                  List<ContactPhoneNumberDto> contactPhoneNumberDtoList) {
        log.info("Update contact phone numbers by userId: {}", userId);
        contactPhoneNumberRepository.deleteAllByUserId(userId);
        friendService.makeFriends(userId,
                contactPhoneNumberRepository.saveAll(contactPhoneNumberMapper.map(userId, contactPhoneNumberDtoList)));
    }

}
