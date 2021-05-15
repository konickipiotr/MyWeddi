package com.myweddi.webapp.host;

import com.myweddi.conf.Global;
import com.myweddi.modules.gift.GiftWrapper;
import com.myweddi.modules.gift.model.GiftIn;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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
@RequestMapping("/host/gift")
public class HostGiftController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public HostGiftController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String gift(Model model, Principal principal){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift";
        ResponseEntity<GiftWrapper> response = restTemplate.getForEntity(path, GiftWrapper.class);
        GiftWrapper wrapper = response.getBody();

        model.addAttribute("giftWrapper", wrapper);
        model.addAttribute("giftIn", new GiftIn(wrapper));
        model.addAttribute("menu", Menu.hostMenu);
        return "host/gifts";
    }

    @PostMapping
    public String changeGift(GiftIn giftIn, Principal principal, Model model){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift";
        restTemplate.postForEntity(path, giftIn, Void.class);
        return "redirect:/host/gift";
    }

    @PostMapping("/add")
    public String addGift(@RequestParam("giftname") String giftname, Principal principal,  Model model){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift/add";
        restTemplate.postForEntity(path, giftname, Void.class);
        return "redirect:/host/gift";
    }

    @PostMapping("/remove")
    public String removeGift(@RequestParam("giftid") Long giftid, Principal principal,  Model model){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/gift/remove";
        restTemplate.postForEntity(path, giftid, Void.class);
        return "redirect:/host/gift";
    }

    private UserAuth configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }
}
