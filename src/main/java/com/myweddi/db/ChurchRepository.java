package com.myweddi.db;

import com.myweddi.model.ChurchInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChurchRepository extends JpaRepository<ChurchInfo, Long> {
}
