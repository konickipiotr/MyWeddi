package com.myweddi;

import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.sql.Date;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private WeddingInfoRepository weddingInfoRepository;

    @Autowired
    public HomeController(UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository, WeddingInfoRepository weddingInfoRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.weddingInfoRepository = weddingInfoRepository;
    }

    @GetMapping("/login")
    public String login(Model model, String message, String errormessage){
        model.addAttribute("message", message);
        model.addAttribute("errormessage", errormessage);
        return "login";
    }

    @GetMapping
    public String home(Principal principal, HttpSession session){
        if(principal == null)
            return "redirect:/logout";
        UserAuth userAuth = userAuthRepository.findByUsername(principal.getName());

        String view = "";
        Role role = Role.valueOf(userAuth.getRole());
        session.setAttribute("role", role);
        session.setAttribute("userid", userAuth.getId());
        Host host = null;
        SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy");
        switch (role){
            case ADMIN: view = "redirect:/admin"; break;
            case HOST:{
                host = this.hostRepository.findById(userAuth.getId()).get();
                session.setAttribute("bridename", host.getBrideName());
                session.setAttribute("groomname", host.getGroomName());
                session.setAttribute("profilePhoto", host.getWebAppPath());
                WeddingInfo weddingInfo = this.weddingInfoRepository.findById(host.getId()).get();
                if(weddingInfo.getCeremenytime() != null){
                    String sDate = sd.format(Date.valueOf(weddingInfo.getCeremenytime().toLocalDate()));
                    session.setAttribute("weddingdate", sDate);
                }

                view = "redirect:/home";
            } break;
            case NEWGUEST:{
                    return "redirect:/firstlogin";
            }
            case GUEST:{
                Guest guest = this.guestRepository.findById(userAuth.getId()).get();
                host = this.hostRepository.findById(guest.getWeddingid()).get();
                session.setAttribute("bridename", host.getBrideName());
                session.setAttribute("groomname", host.getGroomName());
                session.setAttribute("profilePhoto", guest.getWebAppPath());
                WeddingInfo weddingInfo = this.weddingInfoRepository.findById(host.getId()).get();
                if(weddingInfo.getCeremenytime() != null){
                    String sDate = sd.format(Date.valueOf(weddingInfo.getCeremenytime().toLocalDate()));
                    session.setAttribute("weddingdate", sDate);
                }
                view = "redirect:/home";
            } break;
            case DJ:  view = "redirect:/dj"; break;
            case PHOTOGRAPHER:  view = "redirect:/photographer"; break;
            default: view = "login";
        }

        return view;
    }

}
