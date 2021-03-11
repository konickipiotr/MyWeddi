package com.myweddi.webapp.host.guest;

import com.myweddi.conf.Global;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/host/guest")
public class GuestController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public GuestController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String toGuestView(Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());

        String path = Global.domain + "/api/guest/" + user.getId();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);
        List<Guest> guestList = response.getBody().getList();

        model.addAttribute("guestList", guestList);
        model.addAttribute("menu", Menu.hostMenu);
        return "host/addguest";
    }

    @PostMapping
    public String addGuest(@RequestParam("firstname") String firstname,
                           @RequestParam("lastname") String lastname,
                           Model model,
                           Principal principal ){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());

        Guest guest = new Guest(user.getId(), firstname, lastname);

        String path = Global.domain + "/api/guest/";
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<Void> response = restTemplate.postForEntity(path, guest, Void.class);

        return "redirect:/host/guest";
    }
}
