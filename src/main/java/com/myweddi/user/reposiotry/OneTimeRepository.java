package com.myweddi.user.reposiotry;

import com.myweddi.user.OneTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimeRepository extends JpaRepository<OneTime, Long> {
}
