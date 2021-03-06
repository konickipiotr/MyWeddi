package com.myweddi.webapp;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Posttype;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.MultipartInputStreamFileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/profilphoto")
public class ProfilePhotoController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ProfilePhotoController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public String uploadProfilePhoto(MultipartFile profilePhoto, String returnView, Principal principal, HttpSession session){
        UserAuth userAuth = configRestTemplate(principal.getName());

        String path =   Global.domain + "/api/profilephoto/"+ userAuth.getId();

        List<String> sImage = new ArrayList<>();
        if (!profilePhoto.isEmpty()) {
            try {
                byte[] bytes = profilePhoto.getBytes();
                sImage.add(Base64.getEncoder().encodeToString(bytes));
            } catch (IOException e) {
                throw new FailedSaveFileException();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> requestEntity = new HttpEntity<Object>(sImage, headers);
        restTemplate.postForObject(path, requestEntity, String.class);

        path =   Global.domain + "/api/user";
        ResponseEntity<User> response = restTemplate.getForEntity(path, User.class);
        User user = response.getBody();

        session.setAttribute("profilePhoto", user.getWebAppPath());

        if(returnView == null)
            return "redirect:/";

        switch (returnView){
            case "GUESTSETTINGS": return "redirect:/guest/settings";
            case "HOSTSETTINGS": return "redirect:/host/settings";
        }
        return "redirect:/";
    }

    private UserAuth configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }
}
