package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.model.WeddiLike;
import com.myweddi.user.FirstLoginForm;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/firstlogin")
public class FirstLoginController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public FirstLoginController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String firstlogin(){
        return "guest/first_visit";
    }

    @PostMapping
    public String firstloginAccept(FirstLoginForm firstLoginForm, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());

        String path = Global.domain + "/api/firstlogin";
        restTemplate.postForEntity(path, firstLoginForm, Void.class);

        user.setRole("GUEST");
        Authentication reAuth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(reAuth);
        return "redirect:/";
    }

    private UserAuth  configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }
}
