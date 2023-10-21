package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_entity")
public class UserEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @Column(name = "primary_phone_number")
    @Pattern(regexp = "^[0-9]{10}$", message = "primary phone number should be 10 digits")
    private String primaryPhoneNumber;
}
