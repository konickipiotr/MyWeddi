package com.myweddi;

import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;

    @Autowired
    public HomeController(UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping
    public String home(Principal principal, HttpSession session){
        UserAuth userAuth = userAuthRepository.findByUsername(principal.getName());

        String view = "";
        Role role = Role.valueOf(userAuth.getRole());
        session.setAttribute("role", role);

        Host host = null;
        switch (role){
            case ADMIN: view = "redirect:/admin"; break;
            case HOST:{
                host = this.hostRepository.findById(userAuth.getId()).get();
                session.setAttribute("bridename", host.getBrideName());
                session.setAttribute("groomname", host.getGroomName());
                session.setAttribute("profilePhoto", host.getWebAppPath());
                view = "redirect:/home";
            } break;
            case GUEST:{
                Guest guest = null;
                guest = this.guestRepository.findById(userAuth.getId()).get();
                host = this.hostRepository.findById(guest.getWeddingid()).get();
                session.setAttribute("bridename", host.getBrideName());
                session.setAttribute("groomname", host.getGroomName());
                session.setAttribute("profilePhoto", guest.getWebAppPath());
                view = "redirect:/home";
            } break;
            case DJ:  view = "redirect:/dj"; break;
            case PHOTOGRAPHER:  view = "redirect:/photographer"; break;
        }

        return view;
    }

}
