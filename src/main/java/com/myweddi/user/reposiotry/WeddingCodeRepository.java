package com.myweddi.user.reposiotry;

import com.myweddi.user.WeddingCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingCodeRepository extends JpaRepository<WeddingCode, Long> {
    boolean existsByWeddingcode(String weddingcode);
    WeddingCode findByWeddingcode(String weddingcode);
}
