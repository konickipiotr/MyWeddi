package com.myweddi.webapp.host;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class HostHomeController {

    @GetMapping
    public String home(Model model){

        model.addAttribute("menu", Menu.hostMenu);
        return "owner/home";
    }
}
