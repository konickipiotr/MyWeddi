package com.myweddi.db;

import com.myweddi.conf.Global;
import com.myweddi.model.*;
import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.OneTimeRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private UserAuthRepository userAuthRepository;
    private PasswordEncoder passwordEncoder;
    private ChurchRepository churchRepository;
    private WeddingInfoRepository weddingInfoRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private OneTimeRepository oneTimeRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private PhotoRepository photoRepository;
    private TablesRepository tablesRepository;
    private TablePlaceRepository tablePlaceRepository;

    @Autowired
    public DbInit(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, ChurchRepository churchRepository, WeddingInfoRepository weddingInfoRepository, HostRepository hostRepository, GuestRepository guestRepository, OneTimeRepository oneTimeRepository, PostRepository postRepository, CommentRepository commentRepository, PhotoRepository photoRepository, TablesRepository tablesRepository, TablePlaceRepository tablePlaceRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.churchRepository = churchRepository;
        this.weddingInfoRepository = weddingInfoRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.oneTimeRepository = oneTimeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.tablesRepository = tablesRepository;
        this.tablePlaceRepository = tablePlaceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userAuthRepository.deleteAll();
        this.weddingInfoRepository.deleteAll();
        this.churchRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.guestRepository.deleteAll();
        this.oneTimeRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.photoRepository.deleteAll();
        this.tablePlaceRepository.deleteAll();
        this.tablesRepository.deleteAll();

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

        UserAuth ua6 = new UserAuth("sg2", passwordEncoder.encode("11"), "GUEST", UserStatus.ACTIVE);
        this.userAuthRepository.save(ua6);

        ChurchInfo churchInfo1 = new ChurchInfo();
        churchInfo1.setWeddingid(ua2.getId());
        churchInfo1.setCeremenytime(LocalDateTime.of(2021,06,16,17,00));
        churchInfo1.setLatitude(50.294266);
        churchInfo1.setLongitude(16.8728048);
        churchInfo1.setAddress("Mickiewicza 2/45, 57-550 Stronie Ślaskie");
        churchInfo1.setName("Kościół M.B. Królowej Polski i św. Maternusa");
        churchInfo1.setRealPath("https://lh3.googleusercontent.com/proxy/hbz2DwHE6bkH7YviqQVPmX6ummHKpdpC3wQKOBCNnmhFYM5OFeH7P6XnqPVl5qqC_2LmPdpWTfnxrgO7VFssLzj-ApI6XSZ6o_uv3WFwKRmUKBUa5lE0xJZJInvj6g");
        //churchInfo1.setWebAppPath("/church/church.jpg");
        churchInfo1.setWebAppPath("https://polska-org.pl/foto/6932/Kosciol_Matki_Bozej_Krolowej_Polski_i_sw_Maternusa_ul_Koscielna_Stronie_Slaskie_6932428.jpg");
        this.churchRepository.save(churchInfo1);

        WeddingInfo weddingInfo1 = new WeddingInfo();
        weddingInfo1.setWeddingid(ua2.getId());
        weddingInfo1.setLatitude(50.2827394);
        weddingInfo1.setLongitude(16.880499);
        weddingInfo1.setAddress("Kochanowskiego 13, 57-550 Stronie Ślaskie");
        weddingInfo1.setName("Górski Poranek");
        //weddingInfo1.setWebAppPath("/weddinghouse/gorskiporanek.jpg");
        weddingInfo1.setWebAppPath("https://meteor-turystyka.pl/images/base/16/15079/30937_40.jpg");
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
        h1.setWebAppPath("");
        this.hostRepository.save(h1);

        Guest g1 = new Guest();
        g1.setId(ua3.getId());
        g1.setWeddingid(ua2.getId());
        g1.setRole("GUEST");
        g1.setFirstname("Jola");
        g1.setLastname("Patola");
        g1.setStatus(GuestStatus.NOTCONFIRMED);
        this.guestRepository.save(g1);

        Guest g2 = new Guest();
        g2.setId(ua6.getId());
        g2.setWeddingid(ua2.getId());
        g2.setRole("GUEST");
        g2.setFirstname("Adam");
        g2.setLastname("Nowak");
        g2.setStatus(GuestStatus.CONFIRMED);
        this.guestRepository.save(g2);

        LocalDateTime localDateTime = LocalDateTime.now(Global.zid);
        Post p1 = new Post(ua2.getId(), ua2.getId(), localDateTime, "takietam", Posttype.LOCAL);
        this.postRepository.save(p1);

        Photo ph1 = new Photo(p1.getId(), ua2.getId());
        ph1.setRealPath("/home/piterk/myweddi/photos/aa.jpg");
        ph1.setWebAppPath("https://www.slubnaglowie.pl/media/cache/content_max/uploads/media/post/0003/41/74f7f3b1992d46b5101ee14adbdeaf0380272729.jpeg");
        this.photoRepository.save(ph1);

        Comment com1 = new Comment(p1.getId(), ua3.getId(), "Super zdjęcie", LocalDateTime.now(Global.zid));
        this.commentRepository.save(com1);


        int tableid[] = {1,2,3};
        int capacity[] = {3,4,1};
        Tables tables = new Tables(ua2.getId(), capacity.length, capacity);
        this.tablesRepository.save(tables);

        List<TablePlace> tp = new ArrayList<>();
        for(int i = 0; i < tableid.length; i++){

            for(int j = 0; j < capacity[i]; j++)
                tp.add(new TablePlace(tableid[i], (j + 1), ua2.getId()));
        }
        this.tablePlaceRepository.saveAll(tp);
    }
}
