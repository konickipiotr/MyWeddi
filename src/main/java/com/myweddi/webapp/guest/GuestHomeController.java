package com.myweddi.webapp.guest;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/guest")
public class GuestHomeController {

    @GetMapping
    public String home(Model model){

        model.addAttribute("menu", Menu.guestMenu);
        return "guest/home";
    }
}
