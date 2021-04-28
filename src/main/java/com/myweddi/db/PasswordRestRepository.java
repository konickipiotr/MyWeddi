package com.myweddi.db;

import com.myweddi.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRestRepository extends JpaRepository<PasswordReset, Long> {

    boolean existsByPasswordcode(String passwordcode);
    PasswordReset findByPasswordcode(String passwordcode);
}
