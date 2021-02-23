package com.myweddi.webapp.photographer;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photographer")
public class PhotographerHomeController {

    @GetMapping
    public String home(Model model){

        return "photographer/home";
    }
}
