package com.myweddi.api;

import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/firstlogin")
public class FirstLoginAPIController {

    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;

    @Autowired
    public FirstLoginAPIController(GuestRepository guestRepository, UserAuthRepository userAuthRepository) {
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @PostMapping
    public ResponseEntity firstloginAccept(@RequestBody FirstLoginForm firstLoginForm, Principal principal){

        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        user.setRole("GUEST");
        user.setStatus(UserStatus.ACTIVE);
        guest.setFirstLoginData(firstLoginForm);

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("GUEST"));

        this.userAuthRepository.save(user);
        this.guestRepository.save(guest);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/api")
    public ResponseEntity firstApiLoginAccept(@RequestBody Guest guest, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        if(guest.getStatus().equals(GuestStatus.CONFIRMED)) {
            user.setRole("GUEST");
            user.setStatus(UserStatus.ACTIVE);
        }

        this.userAuthRepository.save(user);
        this.guestRepository.save(guest);

        return new ResponseEntity(HttpStatus.OK);
    }
}
