package com.myweddi.api;

import com.myweddi.modules.gift.GiftService;
import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.modules.gift.model.GiftIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/gift")
public class GiftAPIController {

    @Autowired
    private GiftService giftService;

    @GetMapping
    public ResponseEntity<GiftWrapper> getWeddingGifts(Principal principal){
        return new ResponseEntity<GiftWrapper>(giftService.getGiftsInfo(principal.getName()), HttpStatus.OK);
    }

    @PostMapping
    public void changeGifts(@RequestBody GiftIn gIn, Principal principal){
        giftService.changeGift(gIn, principal.getName());
    }

    @PostMapping("/add")
    public void saveGift(@RequestBody String name, Principal principal){
        giftService.saveGift(name, principal.getName());
    }

    @PostMapping("/remove")
    public void removerGift(@RequestBody Long giftid, Principal principal){
        giftService.removeGift(giftid, principal.getName());
    }
}
