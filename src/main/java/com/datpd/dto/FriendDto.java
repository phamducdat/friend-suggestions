package com.datpd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendDto {

    private long friendId;
    private String friendName;
    private String friendPhoneNumber;
}
