package com.myweddi.api;

import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.modules.gift.model.GiftIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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
    public void saveGift(@RequestBody Map<String, String> body, Principal principal){

        giftService.saveGift(body.get("name"), principal.getName());
    }

    @PostMapping("/remove")
    public void removerGift(@RequestBody Long giftid, Principal principal){
        giftService.removeGift(giftid, principal.getName());
    }

    @PostMapping("/book")
    public ResponseEntity bookGift(@RequestBody Long giftid, Principal principal){
        return giftService.bookGift(giftid, principal.getName());
    }

    @PostMapping("/unbook")
    public ResponseEntity<String> unbookGift(@RequestBody Long giftid, Principal principal){
        return giftService.unbookGift(giftid, principal.getName());
    }
}
