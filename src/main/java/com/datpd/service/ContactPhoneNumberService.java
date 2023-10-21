package com.datpd.service;

import com.datpd.dto.ContactPhoneNumbersDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.mapper.ContactPhoneNumberMapper;
import com.datpd.repository.ContactPhoneNumberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactPhoneNumberService {

    private final ContactPhoneNumberRepository contactPhoneNumberRepository;
    private final ContactPhoneNumberMapper contactPhoneNumberMapper;

    public ContactPhoneNumberService(ContactPhoneNumberRepository contactPhoneNumberRepository, ContactPhoneNumberMapper contactPhoneNumberMapper) {
        this.contactPhoneNumberRepository = contactPhoneNumberRepository;
        this.contactPhoneNumberMapper = contactPhoneNumberMapper;
    }

    private ContactPhoneNumbersDto getContactPhoneNumbersByUserId(long userId) {
        List<ContactPhoneNumberEntity> contactPhoneNumberEntities =
                contactPhoneNumberRepository.getContactPhoneNumberEntitiesByUserId(userId);

        return contactPhoneNumberEntities.isEmpty() ? null : contactPhoneNumberMapper.map(contactPhoneNumberEntities);
    }

    private void updateContactPhoneNumbersByUserId(long userId, ContactPhoneNumbersDto contactPhoneNumbersDto) {
        contactPhoneNumberRepository.deleteAllByUserId(userId);
        contactPhoneNumberRepository.saveAll(contactPhoneNumberMapper.map(contactPhoneNumbersDto));
    }
}
