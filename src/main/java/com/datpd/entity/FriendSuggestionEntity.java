package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "friend_suggestion_entity")
public class FriendSuggestionEntity {

    @Id
    @GeneratedValue
    private long id;
    private long userTargetId;
    private String userTargetName;
    private String userPhoneTarget;
    private long userFriendSuggestionId;
    private String userFriendNameSuggestion;
    private String userFriendPhoneSuggestion;

}
