package com.myweddi.webapp.host.registration;

import com.myweddi.conf.Global;
import com.myweddi.model.Activation;
import com.myweddi.user.RegistrationForm;
import com.myweddi.user.UserAuth;
import com.myweddi.user.UserStatus;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserAuthRepository userAuthRepository;
    @Autowired
    private RestTemplate restTemplate;


    @GetMapping
    public String toRegistration(Model model){
        RegistrationForm rf = new RegistrationForm();
        rf.setBridefirstname("Anna");
        rf.setBridelastname("Nowak");
        rf.setGroomfirstname("Janusz");
        rf.setGroomlastname("Plusz");
        rf.setBrideemail("anna@nowka.com");
        rf.setBridephone("759189");
        rf.setGroomemail("konicki.piotr@gmail.com");
        rf.setGroomphone("7588");
        rf.setUsername("jj");
        rf.setPassword("111111");
        rf.setPassword2("111111");
        model.addAttribute("registrationForm", rf);
        return "registration";
    }

    @PostMapping
    public String newAccountData(RegistrationForm rf, Model model, BindingResult result){
        System.out.println(rf);
        new RegistrationValidator(userAuthRepository).validate(rf, result);
        if(result.hasErrors()){
            model.addAttribute("registrationForm", rf);
            return "registration";
        }

        String path = Global.domain + "/api/registration";
        ResponseEntity<RegistrationForm> response = restTemplate.postForEntity(path, rf, RegistrationForm.class);
        if(response.getStatusCode() == HttpStatus.OK){
            RegistrationForm rr = response.getBody();
            model.addAttribute("userid", rr.getUserid());
        }
        return "registration_complete";
    }

    @GetMapping("/activation/{activationcode}")
    public String activeAccount(@PathVariable("activationcode") String activationCode, Model model, RedirectAttributes ra){

        String path = Global.domain + "/api/registration/activation";
        ResponseEntity<Long> response = null;
        try {
            response = restTemplate.postForEntity(path, activationCode, Long.class);
        }catch (HttpClientErrorException e){
            ra.addAttribute("errormessage", "Błędny kod. Możliwe, że nie aktualny");
            return "redirect:/login";
        }

        model.addAttribute("userid", response.getBody());
        return "activation_success";
    }

    @PostMapping("/sendagain")
    public String activeAccount(Long userid, RedirectAttributes ra){

        String path = Global.domain + "/api/registration/sendagain";
        ResponseEntity<Void> response = restTemplate.postForEntity(path, userid, Void.class);
        if(response.getStatusCode() != HttpStatus.OK){
            return "redirect:/error";
        }
        ra.addAttribute("message", "Ponownie wysłano link aktywacyjny");
        return "redirect:/login";
    }
}
