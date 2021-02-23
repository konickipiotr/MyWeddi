package com.myweddi.info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingInfoRepository extends JpaRepository<WeddingInfo, Long> {
}
