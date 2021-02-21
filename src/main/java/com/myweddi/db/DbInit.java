package com.myweddi.db;

import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DbInit implements CommandLineRunner {

    private UserAuthRepository userAuthRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DbInit(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userAuthRepository.deleteAll();

        UserAuth ua1 = new UserAuth("sa", passwordEncoder.encode("11"), "ADMIN", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua1);

        UserAuth ua2 = new UserAuth("so", passwordEncoder.encode("11"), "OWNER", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua2);

        UserAuth ua3 = new UserAuth("sg", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua3);

        UserAuth ua4 = new UserAuth("sd", passwordEncoder.encode("11"), "DJ", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua4);

        UserAuth ua5 = new UserAuth("sf", passwordEncoder.encode("11"), "PHOTOGRAPHER", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua5);
    }
}
