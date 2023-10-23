package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "friend_suggestion_entity",
        indexes = {@Index(name = "idx_user_target_id", columnList = "user_target_id")}
)
public class FriendSuggestionEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "user_target_id")
    private long userTargetId;

    @Column(name = "user_target_name")
    private String userTargetName;

    @Column(name = "user_target_target")
    private String userPhoneTarget;

    @Column(name = "user_friend_suggestion_id")
    private long userFriendSuggestionId;

    @Column(name = "user_friend_name_suggestion")
    private String userFriendNameSuggestion;
    @Column(name = "user_friend_phone_suggestion")
    private String userFriendPhoneSuggestion;

}
