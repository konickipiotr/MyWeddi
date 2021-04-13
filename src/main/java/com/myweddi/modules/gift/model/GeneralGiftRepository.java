package com.myweddi.modules.gift.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralGiftRepository extends JpaRepository<GeneralGifts, Long> {
}
