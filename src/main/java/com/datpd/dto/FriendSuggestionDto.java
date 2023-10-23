package com.datpd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendSuggestionDto {


    private String friendSuggestionName;
    private String friendSuggestionPhoneNumber;
}
