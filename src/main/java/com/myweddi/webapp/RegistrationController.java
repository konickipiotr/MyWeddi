package com.myweddi.webapp;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.user.RegistrationForm;
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

    private final UserAuthRepository userAuthRepository;
    private final RestTemplate restTemplate;
    private final String ROOT_PATH = Global.domain + "/api/registration";

    @Autowired
    public RegistrationController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String toRegistration(Model model){
        model.addAttribute("registrationForm", new RegistrationForm());
        return "public/registration/registration";
    }

    @PostMapping
    public String newAccountData(RegistrationForm rf, Model model, BindingResult result){
        new RegistrationValidator(userAuthRepository).validate(rf, result);
        if(result.hasErrors()){
            model.addAttribute("registrationForm", rf);
            return "public/registration/registration";
        }

        String path = ROOT_PATH;
        if(rf.getUsertype().equals("GUEST")){
            path += "/guest";
        }

        ResponseEntity<Long> response;
        try {
            response = restTemplate.postForEntity(path, rf, Long.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                model.addAttribute("errorweddingcode", Msg.errorWeddingCode);
                model.addAttribute("registrationForm", rf);
                return "public/registration/registration";

            }else {
                model.addAttribute("errormessage", Msg.unknownProblem);
                return "error";
            }
        }

        String email;
        if(rf.getUsertype().equals("HOST"))
            email = rf.getBrideemail() + " " + rf.getGroomemail();
        else
            email = rf.getUsername();

        model.addAttribute("email", email);
        model.addAttribute("userid", response.getBody());
        return "public/registration/registration_complete";
    }

    @GetMapping("/activation/{activationcode}")
    public String activeAccount(@PathVariable("activationcode") String activationCode, Model model, RedirectAttributes ra){

        String path = ROOT_PATH + "/activation";
        ResponseEntity<Long> response;
        try {
            response = restTemplate.postForEntity(path, activationCode, Long.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                model.addAttribute("error_message", Msg.errorActivationCode);
                return "login";
            }
            else {
                model.addAttribute("errormessage", Msg.unknownProblem);
                return "error";
            }
        }

        model.addAttribute("message", Msg.info_activationSuccess);
        return "login";
    }

    @PostMapping("/sendagain")
    public String activeAccount(Long userid, Model model){

        String path = ROOT_PATH + "/sendagain";
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(path, userid, String.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                model.addAttribute("error_message", Msg.errorActivationCode);
                return "login";
            }
            else {
                model.addAttribute("errormessage", Msg.unknownProblem);
                return "error";
            }
        }

        model.addAttribute("message", Msg.info_activationLinkSentAgain);
        model.addAttribute("email", response.getBody());
        model.addAttribute("userid", userid);
        return "public/registration/registration_complete";
    }
}
