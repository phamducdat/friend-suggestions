package com.datpd.mapper;

import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.entity.ContactPhoneNumberEntity;
import com.datpd.entity.FriendEntity;
import com.datpd.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactPhoneNumberMapper {




    public ContactPhoneNumberEntity map(long userId, ContactPhoneNumberDto contactPhoneNumberDto) {
        return ContactPhoneNumberEntity.builder()
                .userId(userId)
                .contactUserName(contactPhoneNumberDto.getContactUserName())
                .contactPhoneNumber(contactPhoneNumberDto.getContactPhoneNumber())
                .build();
    }

    public List<ContactPhoneNumberEntity> map(long userId, List<ContactPhoneNumberDto> contactPhoneNumberDtoList) {
        return contactPhoneNumberDtoList.stream().map(contactPhoneNumberDto -> map(userId, contactPhoneNumberDto)).collect(Collectors.toList());
    }

    public ContactPhoneNumberDto map(ContactPhoneNumberEntity contactPhoneNumberEntity) {
        return ContactPhoneNumberDto.builder()
                .contactUserName(contactPhoneNumberEntity.getContactUserName())
                .contactPhoneNumber(contactPhoneNumberEntity.getContactPhoneNumber())
                .build();
    }

    public List<ContactPhoneNumberDto> map(List<ContactPhoneNumberEntity> contactPhoneNumberEntities) {
        return contactPhoneNumberEntities.stream().map(this::map).collect(Collectors.toList());
    }

}
