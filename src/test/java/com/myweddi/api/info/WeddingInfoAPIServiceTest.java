package com.myweddi.api.info;

import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.user.Guest;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.apiguardian.api.API;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WeddingInfoAPIServiceTest {


    private final WeddingInfoAPIService weddingInfoAPIService;
    private final WeddingInfoRepository weddingInfoRepository;
    private final UserAuthRepository userAuthRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;
    private UserAuth user;
    private UserAuth user2;
    private UserAuth user3;
    private UserAuth user4;

    @Autowired
    public WeddingInfoAPIServiceTest(WeddingInfoAPIService weddingInfoAPIService, WeddingInfoRepository weddingInfoRepository, UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.weddingInfoAPIService = weddingInfoAPIService;
        this.weddingInfoRepository = weddingInfoRepository;
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    @BeforeEach
    void setUp() {
        this.weddingInfoRepository.deleteAll();
        this.userAuthRepository.deleteAll();
        this.hostRepository.deleteAll();
        this.guestRepository.deleteAll();

        user = new UserAuth("host1", "11", "HOST", UserStatus.ACTIVE);
        user2 = new UserAuth("host2", "11", "HOST", UserStatus.ACTIVE);
        user3 = new UserAuth("guest1", "11", "GUEST", UserStatus.ACTIVE);
        user4 = new UserAuth("guest2", "11", "GUEST", UserStatus.ACTIVE);

        this.userAuthRepository.save(user);
        this.userAuthRepository.save(user2);
        this.userAuthRepository.save(user3);
        this.userAuthRepository.save(user4);

        Guest guest = new Guest(user3.getId(), user.getId(), "jan@nowak.com", "Jan", "Nowak");
        Guest guest2 = new Guest(user3.getId(), user2.getId(), "jadam@nowak.com", "Adam", "Nowak");
        this.hostRepository.save(new Host(user.getId()));
        this.hostRepository.save(new Host(user2.getId()));
        this.guestRepository.save(guest);
        this.guestRepository.save(guest2);
    }

    @Test
    void person_not_connected_to_wedding_try_get_weddingInfo() {
        setStandardWeddingInfo();

        ResponseEntity<WeddingInfo> response = this.weddingInfoAPIService.getWeddingInfo(user.getId(), user3.getUsername());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void first_time_save_weddingInfo() {
        List<WeddingInfo> all = this.weddingInfoRepository.findAll();
        assertEquals(0, all.size());
        WeddingInfo weddingInfo = new WeddingInfo(user.getId());

        ResponseEntity<Long> response = this.weddingInfoAPIService.addWeddingInfo(weddingInfo, user.getUsername());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        all = this.weddingInfoRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(weddingInfo.getWeddingid(), all.get(0).getWeddingid());
    }

    @Test
    void update_weddingInfo_return_status_found_and_do_not_change_photo_url() {
        WeddingInfo weddingInfo = setStandardWeddingInfo();
        this.weddingInfoRepository.save(weddingInfo);

        weddingInfo.setChurchname("church2");
        weddingInfo.setWeddinghousename("wedding2");
        weddingInfo.setChWebAppPath("bird");
        weddingInfo.setChRealPath("bird2");
        weddingInfo.setwWebAppPath("fish");
        weddingInfo.setwRealPath("fish2");

        ResponseEntity<Long> response = this.weddingInfoAPIService.addWeddingInfo(weddingInfo, user.getUsername());

        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        Long id = response.getBody();
        assertEquals(weddingInfo.getWeddingid(), id);

        WeddingInfo wi = this.weddingInfoRepository.findAll().get(0);

        assertTrue(weddingInfo.getChurchname().equals(wi.getChurchname()));
        assertTrue(weddingInfo.getWeddinghousename().equals(wi.getWeddinghousename()));
        assertTrue("dog".equals(wi.getChWebAppPath()));
        assertTrue("dog1".equals(wi.getChRealPath()));
        assertTrue("cat".equals(wi.getwWebAppPath()));
        assertTrue("cat1".equals(wi.getwRealPath()));
    }

    @Test
    void forbid_guest_change_weddinginfo() {
        WeddingInfo weddingInfo = setStandardWeddingInfo();

        weddingInfo.setChurchname("XX");
        ResponseEntity<Long> response = this.weddingInfoAPIService.addWeddingInfo(weddingInfo, user3.getUsername());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    private WeddingInfo setStandardWeddingInfo(){
        WeddingInfo weddingInfo = new WeddingInfo(user.getId());
        weddingInfo.setCeremenytime(LocalDateTime.of(2021,06,16,17,00));
        weddingInfo.setChLatitude(50.1);
        weddingInfo.setChLongitude(16.2);
        weddingInfo.setChurchname("Church");
        weddingInfo.setChurchaddress("church_address");
        weddingInfo.setInfo("eloelo");

        weddingInfo.setWeddinghousename("wedding_house");
        weddingInfo.setwAddress("wedding_houses_address");
        weddingInfo.setwLatitude(51.3);
        weddingInfo.setwLongitude(15.4);

        weddingInfo.setChWebAppPath("dog");
        weddingInfo.setChRealPath("dog1");
        weddingInfo.setwWebAppPath("cat");
        weddingInfo.setwRealPath("cat1");
        this.weddingInfoRepository.save(weddingInfo);
        return weddingInfo;
    }
}