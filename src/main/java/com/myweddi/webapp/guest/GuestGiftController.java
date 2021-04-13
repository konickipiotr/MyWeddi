package com.myweddi.webapp.guest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest/gift")
public class GuestGiftController {

    @GetMapping
    public String gift(){
        return "guest/gifts";
    }
}
