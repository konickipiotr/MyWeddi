package com.myweddi.user.reposiotry;

import com.myweddi.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
