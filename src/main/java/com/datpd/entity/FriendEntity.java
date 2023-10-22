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
@Table(name = "friend_entity")
public class FriendEntity {

    @Id
    @GeneratedValue
    private long id;

    private long userId1;

    private String userName1;

    private String phoneNumber1;

    private long userId2;

    private String userName2;

    private String phoneNumber2;
}
