package com.myweddi;

import com.myweddi.user.Role;
import com.myweddi.user.User;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping
    public String home(Principal principal){
        User user = userAuthRepository.findByUsername(principal.getName());
        System.out.println(user);

        String view = "";
        Role role = Role.valueOf(user.getRole());
        switch (role){
            case ADMIN: view = "home"; break;
            case OWNER:  view = "homO"; break;
            case GUEST:  view = "homeG"; break;
            case DJ:  view = "homeD"; break;
            case PHOTOGRAPHER:  view = "homeF"; break;
        }
        return view;
    }

}
