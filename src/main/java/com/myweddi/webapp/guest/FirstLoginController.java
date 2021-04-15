package com.myweddi.webapp.guest;

import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/firstlogin")
public class FirstLoginController {

    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;

    @Autowired
    public FirstLoginController(GuestRepository guestRepository, UserAuthRepository userAuthRepository) {
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @GetMapping
    public String firstlogin(){
        return "guest/first_visit";
    }

    @PostMapping
    public String firstloginAccept(Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        user.setRole("GUEST");
        this.userAuthRepository.save(user);
        guest.setRole("GUEST");
        this.guestRepository.save(guest);
        return "redirect:/";
    }
}
