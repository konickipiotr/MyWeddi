package com.myweddi.user.reposiotry;

import com.myweddi.user.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostRepository extends JpaRepository<Host, Long> {

    Optional<Host> findByBrideemail(String email);
    Optional<Host> findByGroomemail(String email);

    boolean existsByWeddingcode(String weddingcode);
    Host findByWeddingcode(String weddingcode);
}
