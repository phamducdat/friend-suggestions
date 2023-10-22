package com.datpd.repository;

import com.datpd.entity.ContactPhoneNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactPhoneNumberRepository extends JpaRepository<ContactPhoneNumberEntity, Long> {
    List<ContactPhoneNumberEntity> getContactPhoneNumberEntitiesByUserId(long userId);

    ContactPhoneNumberEntity findByUserIdAndContactPhoneNumber(long userId, String contactPhoneNumber);

    List<ContactPhoneNumberEntity> findAllByUserId(long userId);

    void deleteAllByUserId(long userId);

}
