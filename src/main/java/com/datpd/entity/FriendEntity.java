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
@Table(name = "friend_entity",
        indexes = {@Index(name = "idx_user_id_1_user_id_2", columnList = "user_Id_1, user_Id_2")})
public class FriendEntity {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "user_id_1")
    private long userId1;

    @Column(name = "user_name_1")
    private String userName1;

    @Column(name = "phone_number_1")
    private String phoneNumber1;

    @Column(name = "user_id_2")
    private long userId2;

    @Column(name = "user_name_2")
    private String userName2;

    @Column(name = "phone_number_2")
    private String phoneNumber2;
}
