package com.myweddi.api.info;

import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;

@RestController
@RequestMapping("/api/weddinginfo")
public class WeddingInfoAPIController {

    private final WeddingInfoAPIService weddingInfoAPIService;

    @Autowired
    public WeddingInfoAPIController(WeddingInfoAPIService weddingInfoAPIService) {
        this.weddingInfoAPIService = weddingInfoAPIService;
    }

    @PostMapping
    public ResponseEntity<Long> addWeddingInfo(@RequestBody WeddingInfo weddingInfo, Principal principal){
        return weddingInfoAPIService.addWeddingInfo(weddingInfo, principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingInfo> getWeddingInfo(@PathVariable("id") Long weddingid, Principal principal){
        return weddingInfoAPIService.getWeddingInfo(weddingid, principal.getName());
    }

    @PostMapping("/{weddingid}/churchphoto")
    public ResponseEntity<Void> saveChurchPhoto(@PathVariable("weddingid") Long weddingid, MultipartFile photo){
        return weddingInfoAPIService.saveWeddingInfoPhoto(weddingid, photo, PhotoCat.CHURCH);
    }

    @PostMapping("/{weddingid}/weddinghousephoto")
    public ResponseEntity<Void> saveWeddingHousePhoto(@PathVariable("weddingid") Long weddingid, MultipartFile photo){
        return weddingInfoAPIService.saveWeddingInfoPhoto(weddingid, photo, PhotoCat.WEDDINGHOUSE);
    }
}
