package com.myweddi.user.reposiotry;

import com.myweddi.user.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
}
