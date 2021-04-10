package com.myweddi.webapp.host.registration;

import com.myweddi.conf.Global;
import com.myweddi.user.RegistrationForm;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private RestTemplate restTemplate;


    @GetMapping
    public String toRegistration(Model model){
        model.addAttribute("registrationForm", new RegistrationForm());
        return "owner/registration";
    }

    @PostMapping
    public String newAccountData(RegistrationForm rf, Model model, BindingResult result){
        System.out.println(rf);
        new RegistrationValidator(userAuthRepository).validate(rf, result);
        if(result.hasErrors()){
            model.addAttribute("registrationForm", rf);
            return "host/registration";
        }

        String path = Global.domain + "/api/registration";
        ResponseEntity<RegistrationForm> response = restTemplate.postForEntity(path, rf, RegistrationForm.class);
        if(response.getStatusCode() == HttpStatus.OK){
            RegistrationForm rr = response.getBody();
        }
        return "login";
    }
}
