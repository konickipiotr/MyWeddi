package com.myweddi.webapp.owner;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owner")
public class OwnerHomeController {

    @GetMapping
    public String home(Model model){

        return "owner/home";
    }
}
