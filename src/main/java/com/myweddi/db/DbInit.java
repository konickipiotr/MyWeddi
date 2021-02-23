package com.myweddi.db;

import com.myweddi.info.ChurchInfo;
import com.myweddi.info.ChurchRepository;
import com.myweddi.info.WeddingInfo;
import com.myweddi.info.WeddingInfoRepository;
import com.myweddi.user.User;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DbInit implements CommandLineRunner {

    private UserAuthRepository userAuthRepository;
    private PasswordEncoder passwordEncoder;
    private ChurchRepository churchRepository;
    private WeddingInfoRepository weddingInfoRepository;

    @Autowired
    public DbInit(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, ChurchRepository churchRepository, WeddingInfoRepository weddingInfoRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.churchRepository = churchRepository;
        this.weddingInfoRepository = weddingInfoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userAuthRepository.deleteAll();
        this.weddingInfoRepository.deleteAll();
        this.churchRepository.deleteAll();

        User ua1 = new User("sa", passwordEncoder.encode("11"), "ADMIN", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua1);

        User ua2 = new User("so", passwordEncoder.encode("11"), "OWNER", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua2);

        User ua3 = new User("sg", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua3);

        User ua4 = new User("sd", passwordEncoder.encode("11"), "DJ", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua4);

        User ua5 = new User("sf", passwordEncoder.encode("11"), "PHOTOGRAPHER", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua5);

        ChurchInfo churchInfo1 = new ChurchInfo();
        churchInfo1.setWeddingid(ua2.getId());
        churchInfo1.setCeremenytime(LocalDateTime.of(2021,06,16,17,00));
        churchInfo1.setLatitude(50.2942664809475);
        churchInfo1.setLongitude(16.872804810691733);
        churchInfo1.setName("Kościół M.B. Królowej Polski i św. Maternusa");
        churchInfo1.setRealPath("https://lh3.googleusercontent.com/proxy/hbz2DwHE6bkH7YviqQVPmX6ummHKpdpC3wQKOBCNnmhFYM5OFeH7P6XnqPVl5qqC_2LmPdpWTfnxrgO7VFssLzj-ApI6XSZ6o_uv3WFwKRmUKBUa5lE0xJZJInvj6g");
        this.churchRepository.save(churchInfo1);

        WeddingInfo weddingInfo1 = new WeddingInfo();
        weddingInfo1.setWeddingid(ua2.getId());
        weddingInfo1.setWeddingtime(LocalDateTime.of(2021, 06 , 16, 18, 00));
        weddingInfo1.setLatitude(50.28273942394719);
        weddingInfo1.setLongitude(16.88049911450626);
        weddingInfo1.setName("Górski Poranek");
        weddingInfo1.setWebAppPath("https://e-turysta.pl/zdjecia/galeria-glowna/maxw772maxh580/79/Gorski-Poranek-Stronie-Slaskie-799261.jpg");
        this.weddingInfoRepository.save(weddingInfo1);


    }
}
