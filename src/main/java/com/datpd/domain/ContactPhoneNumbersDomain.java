package com.datpd.domain;

import com.datpd.dto.ContactPhoneNumberDto;
import com.datpd.utils.ApiTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContactPhoneNumbersDomain {
    private long userId;
    private List<ContactPhoneNumberDto> contactPhoneNumberDtoList;
    private ApiTypeEnum apiType;
}
