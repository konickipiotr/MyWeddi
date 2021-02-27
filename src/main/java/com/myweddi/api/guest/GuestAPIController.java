package com.myweddi.api.guest;

import com.myweddi.user.Guest;
import com.myweddi.utils.ListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guest")
public class GuestAPIController {

    @Autowired
    private GuestService guestService;

    @GetMapping("/{weddingid}")
    public ResponseEntity<ListWrapper<Guest>> getGuestList(@PathVariable("weddingid") Long weddingid) {
        return new ResponseEntity<ListWrapper<Guest>>(this.guestService.getGuestList(weddingid), HttpStatus.OK);
    }

    @PostMapping
    public void addGuest(@RequestBody Guest guest){
        guestService.addGuest(guest);
    }
}
