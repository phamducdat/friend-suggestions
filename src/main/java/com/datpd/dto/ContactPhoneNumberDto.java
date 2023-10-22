package com.datpd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContactPhoneNumberDto {
    private String contactUserName;
    private String contactPhoneNumber;
}
