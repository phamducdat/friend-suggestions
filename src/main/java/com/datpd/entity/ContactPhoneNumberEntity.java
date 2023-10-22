package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "contact_phone_number_entity")
public class ContactPhoneNumberEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private long userId;

    private String contactUserName;

    private String contactPhoneNumber;

}
