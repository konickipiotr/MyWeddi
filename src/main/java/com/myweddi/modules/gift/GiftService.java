package com.myweddi.modules.gift;

import com.myweddi.modules.gift.model.*;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
        wrapper.setGifts(this.giftRepository.findByWeddingid(user.getWeddingid()));
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

    private User getUser(String username){
        UserAuth ua = this.userAuthRepository.findByUsername(username);
        User user = null;

        if(ua.getRole().equals("HOST")){
            user = new User(this.hostRepository.findById(ua.getId()).get());
        }else if(ua.getRole().equals("GUEST")){
            user = new User(this.guestRepository.findById(ua.getId()).get());
        }
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
