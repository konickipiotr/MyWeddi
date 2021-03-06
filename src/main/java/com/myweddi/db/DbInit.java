package com.myweddi.db;

import com.myweddi.conf.Global;
import com.myweddi.model.*;
import com.myweddi.modules.gift.model.GeneralGiftRepository;
import com.myweddi.modules.gift.model.GeneralGifts;
import com.myweddi.modules.gift.model.Gift;
import com.myweddi.modules.gift.model.GiftRepository;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
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
import java.util.Arrays;
import java.util.List;

@Service
public class DbInit implements CommandLineRunner {

    private UserAuthRepository userAuthRepository;
    private PasswordEncoder passwordEncoder;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private OneTimeRepository oneTimeRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private PhotoRepository photoRepository;
    private TablesRepository tablesRepository;
    private TablePlaceRepository tablePlaceRepository;
    private GiftRepository giftRepository;
    private GeneralGiftRepository generalGiftRepository;
    private WeddingInfoRepository weddingInfoRepository;

    private ActivationRepository activationRepository;
    private PasswordRestRepository passwordRestRepository;


    @Autowired
    public DbInit(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, HostRepository hostRepository, GuestRepository guestRepository, OneTimeRepository oneTimeRepository, PostRepository postRepository, CommentRepository commentRepository, PhotoRepository photoRepository, TablesRepository tablesRepository, TablePlaceRepository tablePlaceRepository, GiftRepository giftRepository, GeneralGiftRepository generalGiftRepository, WeddingInfoRepository weddingInfoRepository, ActivationRepository activationRepository, PasswordRestRepository passwordRestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.oneTimeRepository = oneTimeRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.tablesRepository = tablesRepository;
        this.tablePlaceRepository = tablePlaceRepository;
        this.giftRepository = giftRepository;
        this.generalGiftRepository = generalGiftRepository;
        this.weddingInfoRepository = weddingInfoRepository;
        this.activationRepository = activationRepository;
        this.passwordRestRepository = passwordRestRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.userAuthRepository.deleteAll();
        this.weddingInfoRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.guestRepository.deleteAll();
        this.oneTimeRepository.deleteAll();
        this.postRepository.deleteAll();
        this.commentRepository.deleteAll();
        this.photoRepository.deleteAll();
        this.tablePlaceRepository.deleteAll();
        this.tablesRepository.deleteAll();
        this.giftRepository.deleteAll();
        this.generalGiftRepository.deleteAll();

        activationRepository.deleteAll();
        passwordRestRepository.deleteAll();

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

        UserAuth ua6 = new UserAuth("sg2", passwordEncoder.encode("11"), "NEWGUEST", UserStatus.FIRSTLOGIN);
        this.userAuthRepository.save(ua6);


        WeddingInfo wi1 = new WeddingInfo(ua2.getId());
        wi1.setWeddingid(ua2.getId());
        wi1.setCeremenytime(LocalDateTime.of(2021,06,16,17,00));
        wi1.setChLatitude(50.25);
        wi1.setChLongitude(16.58);
        wi1.setChurchname("Kościół M.B. Królowej Polski i św. Maternusa");
        wi1.setChurchaddress("Mickiewicza 2/45, 57-550 Stronie Ślaskie");
        wi1.setInfo("elo elo");

        wi1.setWeddinghousename("Górski Poranek");
        wi1.setwAddress("Kochanowskiego 13, 57-550 Stronie Ślaskie");
        wi1.setwLatitude(50.44);
        wi1.setwLongitude(15.55);

        wi1.setChWebAppPath("https://polska-org.pl/foto/6932/Kosciol_Matki_Bozej_Krolowej_Polski_i_sw_Maternusa_ul_Koscielna_Stronie_Slaskie_6932428.jpg");
        wi1.setwWebAppPath("https://meteor-turystyka.pl/images/base/16/15079/30937_40.jpg");
        this.weddingInfoRepository.save(wi1);


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
        h1.setWebAppPath("https://images-na.ssl-images-amazon.com/images/I/61fwEVytm4L._AC_SL1500_.jpg");
        this.hostRepository.save(h1);

        Guest g1 = new Guest();
        g1.setId(ua3.getId());
        g1.setWeddingid(ua2.getId());
        g1.setFirstname("Jola");
        g1.setLastname("Patola");
        g1.setWebAppPath("/img/user.png");
        g1.setStatus(GuestStatus.NOTCONFIRMED);
        this.guestRepository.save(g1);

        Guest g2 = new Guest();
        g2.setId(ua6.getId());
        g2.setWeddingid(ua2.getId());
        g2.setFirstname("Adam");
        g2.setLastname("Nowak");
        g2.setWebAppPath("/img/user.png");
        g2.setStatus(GuestStatus.CONFIRMED);
        this.guestRepository.save(g2);

        LocalDateTime localDateTime = LocalDateTime.now(Global.zid);
        Post p1 = new Post(ua2.getId(), ua2.getId(), localDateTime, "takietam", Posttype.LOCAL);
        this.postRepository.save(p1);

        Photo ph1 = new Photo(p1.getId(), ua2.getId());
        ph1.setRealPath("/home/piterk/myweddi/photos/aa.jpg");
        ph1.setWebAppPath("https://www.slubnaglowie.pl/media/cache/content_max/uploads/media/post/0003/41/74f7f3b1992d46b5101ee14adbdeaf0380272729.jpeg");
        this.photoRepository.save(ph1);

        this.commentRepository.save(new Comment(p1.getId(), ua3.getId(), "Super zdjęcie", LocalDateTime.now(Global.zid)));
        this.commentRepository.save(new Comment(p1.getId(), ua2.getId(), "No wiem", LocalDateTime.now(Global.zid)));
        this.commentRepository.save(new Comment(p1.getId(), ua3.getId(), "Chcę je dostać", LocalDateTime.now(Global.zid)));
        this.commentRepository.save(new Comment(p1.getId(), ua2.getId(), "ok", LocalDateTime.now(Global.zid)));


        int tableid[] = {1,2,3};

        List<Integer> capacity = Arrays.asList(3,4,1);
        Tables tables = new Tables(ua2.getId(), capacity.size(), capacity);
        this.tablesRepository.save(tables);

        List<TablePlace> tp = new ArrayList<>();
        for(int i = 0; i < tableid.length; i++){

            for(int j = 0; j < capacity.get(i); j++)
                tp.add(new TablePlace(i, j, ua2.getId()));
        }
        this.tablePlaceRepository.saveAll(tp);
        this.generalGiftRepository.save(new GeneralGifts(ua2.getId()));
        this.giftRepository.save(new Gift(ua2.getId(), "Rower"));
        this.giftRepository.save(new Gift(ua2.getId(), "Auto"));
    }
}
