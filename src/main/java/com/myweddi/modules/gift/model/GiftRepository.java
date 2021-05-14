package com.myweddi.modules.gift.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Long> {
    List<Gift> findByWeddingid(Long weddingid);
    Optional<Gift> findByUserid(Long userid);
    void deleteByWeddingid(Long weddingid);
}
