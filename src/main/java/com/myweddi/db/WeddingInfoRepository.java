package com.myweddi.db;

import com.myweddi.model.WeddingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingInfoRepository extends JpaRepository<WeddingInfo, Long> {
}
