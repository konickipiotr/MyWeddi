package com.myweddi.webapp.dj;

import com.myweddi.webapp.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dj")
public class DjHomeController {

    @GetMapping
    public String home(Model model){

        return "dj/home";
    }
}
