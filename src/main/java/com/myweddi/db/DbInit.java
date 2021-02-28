package com.myweddi.db;

import com.myweddi.info.ChurchInfo;
import com.myweddi.info.ChurchRepository;
import com.myweddi.info.WeddingInfo;
import com.myweddi.info.WeddingInfoRepository;
import com.myweddi.user.*;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.OneTimeRepository;
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
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private OneTimeRepository oneTimeRepository;


    @Autowired
    public DbInit(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, ChurchRepository churchRepository, WeddingInfoRepository weddingInfoRepository, HostRepository hostRepository, GuestRepository guestRepository, OneTimeRepository oneTimeRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.churchRepository = churchRepository;
        this.weddingInfoRepository = weddingInfoRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.oneTimeRepository = oneTimeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userAuthRepository.deleteAll();
        this.weddingInfoRepository.deleteAll();
        this.churchRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.guestRepository.deleteAll();
        this.oneTimeRepository.deleteAll();

        UserAuth ua1 = new UserAuth("sa", passwordEncoder.encode("11"), "ADMIN", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua1);

        UserAuth ua2 = new UserAuth("so", passwordEncoder.encode("11"), "HOST", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua2);

        UserAuth ua3 = new UserAuth("sg", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua3);

        UserAuth ua4 = new UserAuth("sd", passwordEncoder.encode("11"), "DJ", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua4);

        UserAuth ua5 = new UserAuth("sf", passwordEncoder.encode("11"), "PHOTOGRAPHER", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua5);

        ChurchInfo churchInfo1 = new ChurchInfo();
        churchInfo1.setWeddingid(ua2.getId());
        churchInfo1.setCeremenytime(LocalDateTime.of(2021,06,16,17,00));
        churchInfo1.setLatitude(50.294266);
        churchInfo1.setLongitude(16.8728048);
        churchInfo1.setAddress("Mickiewicza 2/45, 57-550 Stronie Ślaskie");
        churchInfo1.setName("Kościół M.B. Królowej Polski i św. Maternusa");
        churchInfo1.setRealPath("https://lh3.googleusercontent.com/proxy/hbz2DwHE6bkH7YviqQVPmX6ummHKpdpC3wQKOBCNnmhFYM5OFeH7P6XnqPVl5qqC_2LmPdpWTfnxrgO7VFssLzj-ApI6XSZ6o_uv3WFwKRmUKBUa5lE0xJZJInvj6g");
        churchInfo1.setWebAppPath("https://lh3.googleusercontent.com/proxy/hbz2DwHE6bkH7YviqQVPmX6ummHKpdpC3wQKOBCNnmhFYM5OFeH7P6XnqPVl5qqC_2LmPdpWTfnxrgO7VFssLzj-ApI6XSZ6o_uv3WFwKRmUKBUa5lE0xJZJInvj6g");
        this.churchRepository.save(churchInfo1);

        WeddingInfo weddingInfo1 = new WeddingInfo();
        weddingInfo1.setWeddingid(ua2.getId());
        weddingInfo1.setLatitude(50.2827394);
        weddingInfo1.setLongitude(16.880499);
        weddingInfo1.setAddress("Kochanowskiego 13, 57-550 Stronie Ślaskie");
        weddingInfo1.setName("Górski Poranek");
        weddingInfo1.setWebAppPath("https://e-turysta.pl/zdjecia/galeria-glowna/maxw772maxh580/79/Gorski-Poranek-Stronie-Slaskie-799261.jpg");
        this.weddingInfoRepository.save(weddingInfo1);


        Host h1 = new Host();
        h1.setId(ua2.getId());
        h1.setBridefirstname("Anna");
        h1.setBridelastname("Nowak");
        h1.setBrideemail("anna@nowak.pl");
        h1.setBridephone("54651321");
        h1.setGroomfirstname("Jan");
        h1.setGroomlastname("Kowalski");
        h1.setGroomemail("jan@kowalski.pl");
        h1.setGroomphone("34234234");
        this.hostRepository.save(h1);

        Guest g1 = new Guest();
        g1.setId(ua3.getId());
        g1.setWeddingid(ua2.getId());
        g1.setRole("GUEST");
        g1.setFirstname("Jola");
        g1.setLastname("Patola");
        g1.setStatus(GuestStatus.NOTCONFIRMED);
        this.guestRepository.save(g1);
    }
}
