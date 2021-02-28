package com.myweddi.user.reposiotry;

import com.myweddi.user.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {
    List<Guest> findByWeddingid(Long weddingid);
}
