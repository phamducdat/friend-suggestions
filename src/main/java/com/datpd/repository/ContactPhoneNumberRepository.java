package com.datpd.repository;

import com.datpd.entity.ContactPhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactPhoneNumberRepository extends JpaRepository<ContactPhoneNumberEntity, Long> {
    List<ContactPhoneNumberEntity> getContactPhoneNumberEntitiesByUserId(long userId);

    void deleteAllByUserId(long userId);

}
