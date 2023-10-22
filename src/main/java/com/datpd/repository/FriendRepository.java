package com.datpd.repository;

import com.datpd.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {

    @Query("SELECT f FROM FriendEntity f where f.userId1 = :userId OR f.userId2 = :userId")
    List<FriendEntity> findAllByUserId(@Param("userId") long userId);
}
