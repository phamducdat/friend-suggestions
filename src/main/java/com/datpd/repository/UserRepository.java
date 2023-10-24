package com.datpd.repository;

import com.datpd.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {


    @Query("SELECT ue FROM UserEntity ue " +
            "LEFT JOIN ContactPhoneNumberEntity c ON ue.primaryPhoneNumber = c.contactPhoneNumber AND c.userId = :userId " +
            "LEFT JOIN FriendEntity fe ON ue.primaryPhoneNumber = fe.phoneNumber1 OR ue.primaryPhoneNumber = fe.phoneNumber2 " +
            "WHERE fe.id IS NULL AND c.id IS NOT NULL")
    List<UserEntity> findUserEntitiesNotInFriendForUserId(@Param(value = "userId") long userId);

    UserEntity findByPrimaryPhoneNumber(String phoneNumber);
}
