package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "contact_phone_number_entity",
        indexes = {@Index(name = "idx_user_id_contact_phone_number", columnList = "user_Id, contact_phone_number")}
)
public class ContactPhoneNumberEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Column(name = "user_Id")
    private long userId;

    @Column(name = "contact_user_name")
    private String contactUserName;

    @Column(name = "contact_phone_number")
    private String contactPhoneNumber;

}
