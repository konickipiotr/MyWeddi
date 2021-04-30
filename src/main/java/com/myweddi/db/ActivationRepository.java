package com.myweddi.db;

import com.myweddi.model.Activation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivationRepository extends JpaRepository<Activation, Long> {

    boolean existsByActivationcode(String activationcode);
    Activation findByActivationcode(String activationcode);
}
