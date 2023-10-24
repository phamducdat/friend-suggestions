package com.datpd.repository;

import com.datpd.entity.ContactPhoneNumberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactPhoneNumberRepository extends JpaRepository<ContactPhoneNumberEntity, Long> {
    Page<ContactPhoneNumberEntity> getContactPhoneNumberEntitiesByUserId(long userId, Pageable pageable);

    ContactPhoneNumberEntity findByUserIdAndContactPhoneNumber(long userId, String contactPhoneNumber);

    @Query("SELECT cpne.userId FROM ContactPhoneNumberEntity cpne WHERE cpne.contactPhoneNumber = :phoneNumber AND cpne.userId IN :userIds")
    List<Long> findUserIdsByPhoneNumberAndUserPhoneNumberIds(@Param(value = "phoneNumber") String phoneNumber,
                                                             @Param(value = "userIds") List<Long> userIds);

    List<ContactPhoneNumberEntity> findAllByUserId(long userId);

    void deleteAllByUserId(long userId);

}
