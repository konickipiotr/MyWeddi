package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Controller
@RequestMapping("/guest/gift")
public class GuestGiftController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public GuestGiftController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String gift(Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());

        String path = Global.domain + "/api/gift/";
        ResponseEntity<GiftWrapper> response = restTemplate.getForEntity(path, GiftWrapper.class);

        GiftWrapper giftwrapper = response.getBody();
        model.addAttribute("giftwrapper", giftwrapper);
        model.addAttribute("menu", Menu.guestMenu);
        return "guest/gifts";
    }

    @PostMapping("/book")
    public String bookGift(@RequestParam("giftid") Long giftId, Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift/book";
        ResponseEntity response = restTemplate.postForEntity(path, giftId, Void.class);
        return "redirect:/guest/gift";
    }

    @PostMapping("/unbook")
    public String unbookGift(@RequestParam("giftid") Long giftId, Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift/unbook";
        ResponseEntity<String> response = restTemplate.postForEntity(path, giftId, String.class);
        return "redirect:/guest/gift";
    }

    private UserAuth configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }
}
