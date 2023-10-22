package com.datpd.repository;

import com.datpd.entity.FriendSuggestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendSuggestionRepository extends JpaRepository<FriendSuggestionEntity, Long> {
    List<FriendSuggestionEntity> findAllByUserTargetId(long userId);
}
