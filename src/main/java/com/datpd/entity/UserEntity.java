package com.datpd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_entity",
indexes = {@Index(name = "idx_primary_phone_number", columnList = "primary_phone_number")}
)
public class UserEntity {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "primary_phone_number")
    private String primaryPhoneNumber;
}
