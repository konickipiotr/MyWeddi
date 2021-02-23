package com.myweddi.webapp.admin;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

    @GetMapping
    public String home(Model model){

        model.addAttribute("menu", Menu.adminMenu);
        return "admin/home";
    }
}
