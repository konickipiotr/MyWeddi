package com.myweddi.webapp;

import com.myweddi.exception.NotFoundException;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthServiceWebApp {

    private final UserAuthRepository userAuthRepository;
    private final HostRepository hostRepository;
    private final GuestRepository guestRepository;

    @Autowired
    public AuthServiceWebApp(UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public RestTemplate configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public Long getUserId(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException();
        return user.getId();
    }

    public Long getWeddingid(String username){
        UserAuth userAuth = userAuthRepository.findByUsername(username);
        if(userAuth == null)
            throw new NotFoundException();

        User user = getUser(userAuth);
        return user.getWeddingid();
    }

    private User getUser(UserAuth userAuth){
        User user = null;
        switch (userAuth.getRole()){
            case "GUEST": user = new User(this.guestRepository.findById(userAuth.getId()).get()); break;
            case "HOST": user = new User(this.hostRepository.findById(userAuth.getId()).get()); break;
        }
        user.setRole(userAuth.getRole());
        return user;
    }
}
