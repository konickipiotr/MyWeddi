package com.myweddi.user.reposiotry;

import com.myweddi.user.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<UserAuth, Long> {
    UserAuth findByUsername(String username);
    boolean existsByUsername(String username);
}
