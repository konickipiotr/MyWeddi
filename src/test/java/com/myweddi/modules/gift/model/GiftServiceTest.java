package com.myweddi.modules.gift.model;

import com.myweddi.modules.gift.GiftService;
import com.myweddi.modules.gift.GiftType;
import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.user.Guest;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GiftServiceTest {

    private GiftService giftService;
    private GeneralGiftRepository generalGiftRepository;
    private GiftRepository giftRepository;
    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;

    private UserAuth user1 = new UserAuth("mmm", "11", "HOST", UserStatus.ACTIVE);
    private UserAuth user2 = new UserAuth("jnowak", "11", "GUEST", UserStatus.ACTIVE);
    private Guest guest;

    @Autowired
    public GiftServiceTest(GiftService giftService, GeneralGiftRepository generalGiftRepository, GiftRepository giftRepository, GuestRepository guestRepository, UserAuthRepository userAuthRepository, HostRepository hostRepository) {
        this.giftService = giftService;
        this.generalGiftRepository = generalGiftRepository;
        this.giftRepository = giftRepository;
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
    }

    @BeforeEach
    void setUp() {
        generalGiftRepository.deleteAll();
        userAuthRepository.deleteAll();
        guestRepository.deleteAll();
        giftRepository.deleteAll();
        this.userAuthRepository.save(user1);
        this.hostRepository.save(new Host(user1.getId()));
        this.userAuthRepository.save(user2);
        Guest guest = new Guest(user2.getId(), user1.getId(), "jan.nowek@xx.com", "Jan", "Nowak" );
        this.guestRepository.save(guest);
    }

    @Test
    void get_fresh_gift_info() {
        this.giftService.newAccount(user1.getUsername());
        GiftWrapper giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());

        Map<GiftType, Boolean> smallgifts = giftsInfo.getSelectedGift();
        for(Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()){
            assertFalse(g.getValue());
        }

        assertTrue(giftsInfo.getGifts().isEmpty());
        assertTrue(giftsInfo.getGiftInfo().isEmpty());
        assertEquals(user1.getId(), giftsInfo.getWeddingid());
    }

    @Test
    void get_gift_info_with_customize_small_gifts() {
        this.giftService.newAccount(user1.getUsername());

        GiftWrapper giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        Map<GiftType, Boolean> smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            assertFalse(g.getValue());
        }

        GeneralGifts gg = this.generalGiftRepository.findById(user1.getId()).get();
        gg.setSmallgift(GiftType.BOOKS.name() + ":" + true);
        this.generalGiftRepository.save(gg);

        giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            if(g.getKey().equals(GiftType.BOOKS))
                assertTrue(g.getValue());
            else
                assertFalse(g.getValue());
        }

        gg = this.generalGiftRepository.findById(user1.getId()).get();
        gg.setSmallgift(GiftType.BOOKS.name() + ":" + true + ":" + GiftType.LOTTERY.name() + ":" + true);
        this.generalGiftRepository.save(gg);

        giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            if(g.getKey().equals(GiftType.BOOKS) || g.getKey().equals(GiftType.LOTTERY))
                assertTrue(g.getValue());
            else
                assertFalse(g.getValue());
        }
    }

    @Test
    void set_small_gift() {
        this.giftService.newAccount(user1.getUsername());

        GiftWrapper giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        Map<GiftType, Boolean> smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            assertFalse(g.getValue());
        }
        GiftIn gIn = new GiftIn();
        gIn.setBOOKS(true);
        gIn.setPOTTEDFLOWERS(true);
        gIn.setGiftInfo("elo");

        this.giftService.changeGift(gIn, user2.getUsername());

        giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            switch (g.getKey()){
                case BOOKS:
                case POTTEDFLOWERS: assertTrue(g.getValue()); break;
                default: assertFalse(g.getValue());
            }
        }
        assertTrue("elo".equals(giftsInfo.getGiftInfo()));
    }

    @Test
    void change_small_gift() {
        this.giftService.newAccount(user1.getUsername());

        GeneralGifts gg = this.generalGiftRepository.findById(user1.getId()).get();
        gg.setSmallgift(GiftType.BOOKS.name() + ":" + true + ":" + GiftType.LOTTERY.name() + ":" + true);
        this.generalGiftRepository.save(gg);

        GiftWrapper giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        Map<GiftType, Boolean> smallgifts = giftsInfo.getSelectedGift();
        assertTrue(smallgifts.get(GiftType.BOOKS));
        assertTrue(smallgifts.get(GiftType.LOTTERY));

        GiftIn gIn = new GiftIn();
        gIn.setBOOKS(false);
        gIn.setPOTTEDFLOWERS(false);
        this.giftService.changeGift(gIn, user2.getUsername());

        giftsInfo = this.giftService.getGiftsInfo(user2.getUsername());
        smallgifts = giftsInfo.getSelectedGift();
        for (Map.Entry<GiftType, Boolean> g : smallgifts.entrySet()) {
            assertFalse(g.getValue());
        }
    }
}