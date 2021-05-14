package com.myweddi.api;

import com.myweddi.modules.gift.GiftType;
import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.modules.gift.model.*;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GiftService {

    private GeneralGiftRepository generalGiftRepository;
    private GiftRepository giftRepository;
    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;

    @Autowired
    public GiftService(GeneralGiftRepository generalGiftRepository, GiftRepository giftRepository, GuestRepository guestRepository, UserAuthRepository userAuthRepository, HostRepository hostRepository) {
        this.generalGiftRepository = generalGiftRepository;
        this.giftRepository = giftRepository;
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
    }

    public void newAccount(String username){
        User user = getUser(username);
        this.generalGiftRepository.save(new GeneralGifts(user.getWeddingid()));
    }

    public GiftWrapper getGiftsInfo(String username){
        GiftWrapper wrapper = new GiftWrapper();
        User user = getUser(username);
        GeneralGifts generalGifts = this.generalGiftRepository.findById(user.getWeddingid()).get();
        wrapper.setWeddingid(user.getWeddingid());
        wrapper.setSelectedGift(stringToMap(generalGifts.getSmallgift()));
        wrapper.setGiftInfo(generalGifts.getInfo());
        List<Gift> gifts = this.giftRepository.findByWeddingid(user.getWeddingid());

        boolean reservationImpossible = false;
        for(Gift g : gifts){
            if(g.getUserid() != null && g.getUserid().equals(user.getId())) {
                reservationImpossible = true;
                break;
            }
        }
        wrapper.setReservationImpossible(reservationImpossible);

        if(reservationImpossible){
            for(Gift g : gifts){
                if(g.getUserid() == null) {
                    g.setUsername("NIE MOŻNA");
                }else {
                    g.setUsername(this.guestRepository.findById(g.getUserid()).get().getName());
                }
            }
        }else {
            for(Gift g : gifts){
                if(g.getUserid() != null) {
                    g.setUsername(this.guestRepository.findById(g.getUserid()).get().getName());
                }
            }
        }

        wrapper.setGifts(gifts);
        return wrapper;
    }

    public void changeGift(GiftIn gIn, String username) {
        User user = getUser(username);
        GeneralGifts generalGifts = this.generalGiftRepository.findById(user.getWeddingid()).get();
        generalGifts.setSmallgift(smallGiftsToString(gIn));
        generalGifts.setInfo(gIn.getGiftInfo());
        this.generalGiftRepository.save(generalGifts);
    }

    public void saveGift(String name, String username){
        User user = getUser(username);
        this.giftRepository.save(new Gift(user.getWeddingid(), name));
    }

    public void removeGift(Long giftid, String username) {
        this.giftRepository.deleteById(giftid);
    }

    private String smallGiftsToString(GiftIn gIn){
        Map<GiftType, Boolean> small = gIn.giftsToMap();
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Map.Entry<GiftType, Boolean> gt : small.entrySet()){
            if(first){
                sb.append(gt.getKey().name() + ":" + gt.getValue());
                first = false;
            }else {
                sb.append(":" + gt.getKey().name() + ":" + gt.getValue());
            }
        }
        return sb.toString();
    }

    public ResponseEntity bookGift(Long giftid, String username){
        User user = getUser(username);

        Optional<Gift> oGift = this.giftRepository.findById(giftid);
        if(oGift.isPresent()){
            Gift gift = oGift.get();

            if(gift.getUsername() != null && !gift.getUsername().isBlank())
                return new ResponseEntity(HttpStatus.IM_USED);

            gift.setUserid(user.getId());
            this.giftRepository.save(gift);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> unbookGift(Long giftid, String username) {
        User user = getUser(username);

        Optional<Gift> oGift = this.giftRepository.findById(giftid);
        if(oGift.isPresent()){
            Gift gift = oGift.get();
            gift.setUserid(null);
            this.giftRepository.save(gift);
            return new ResponseEntity<String>("Booked", HttpStatus.OK);
        }
        return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
    }

    private User getUser(String username){
        UserAuth ua = this.userAuthRepository.findByUsername(username);
        User user = null;

        if(ua.getRole().equals("HOST")){
            user = new User(this.hostRepository.findById(ua.getId()).get(), ua.getStatus());
        }else if(ua.getRole().equals("GUEST")){
            user = new User(this.guestRepository.findById(ua.getId()).get(), ua.getStatus());
        }
        user.setRole(ua.getRole());
        return user;
    }

    private Map<GiftType, Boolean> stringToMap(String val){
        Map<GiftType, Boolean> giftMap = new HashMap<>();
        for(GiftType gt : GiftType.values())
            giftMap.put(gt, false);
        if(val != null && !val.isBlank()) {
            String[] elements = val.split(":");
            for(int i = 0; i < elements.length; i+=2){
                GiftType key = GiftType.valueOf(elements[i]);
                Boolean value = Boolean.valueOf(elements[i+1]);
                giftMap.put(key, value);
            }
        }
        return giftMap;
    }
}
