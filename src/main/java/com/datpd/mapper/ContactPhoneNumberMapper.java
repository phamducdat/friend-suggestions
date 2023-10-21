package com.datpd.mapper;

import com.datpd.dto.ContactPhoneNumbersDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactPhoneNumberMapper {

    public List<ContactPhoneNumberEntity> map(ContactPhoneNumbersDto contactPhoneNumbersDto) {
        return contactPhoneNumbersDto.getContactPhoneNumbers().stream().map(s -> {
            return ContactPhoneNumberEntity.builder()
                    .phoneNumber(s)
                    .build();
        }).collect(Collectors.toList());
    }

    public ContactPhoneNumbersDto map(List<ContactPhoneNumberEntity> contactPhoneNumberEntities) {
        return ContactPhoneNumbersDto.builder()
                .contactPhoneNumbers(contactPhoneNumberEntities.stream()
                        .map(ContactPhoneNumberEntity::getPhoneNumber).collect(Collectors.toList())).build();
    }
}
