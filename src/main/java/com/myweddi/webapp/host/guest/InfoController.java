package com.myweddi.webapp.host.guest;

import com.myweddi.conf.Global;
import com.myweddi.info.ChurchInfo;
import com.myweddi.info.WeddingInfo;
import com.myweddi.user.Guest;
import com.myweddi.user.GuestRepository;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/info")
public class InfoController {

    private UserAuthRepository userAuthRepository;
    private GuestRepository guestRepository;
    private RestTemplate restTemplate;

    @Autowired
    public InfoController(UserAuthRepository userAuthRepository, GuestRepository guestRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.guestRepository = guestRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String getInfo(Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();

        String path = Global.domain + "/api/churchinfo/" + guest.getWeddingid();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<ChurchInfo> response = restTemplate.getForEntity(path, ChurchInfo.class);

        path = Global.domain + "/api/weddinginfo/" +  guest.getWeddingid();
        ResponseEntity<WeddingInfo> response2 = restTemplate.getForEntity(path, WeddingInfo.class);
        if(response.getStatusCode() == HttpStatus.OK){
            ChurchInfo churchInfo = response.getBody();
            model.addAttribute("churchInfo", churchInfo);
        }else {
            model.addAttribute("churchInfo", new ChurchInfo(user.getId()));
        }

        if(response2.getStatusCode() == HttpStatus.OK){
            WeddingInfo weddingInfo = response2.getBody();
            model.addAttribute("weddingInfo", weddingInfo);
        }else {
            model.addAttribute("weddingInfo", new WeddingInfo(user.getId()));
        }

        model.addAttribute("menu", Menu.guestMenu);
        return "guest/infoview";
    }
}
