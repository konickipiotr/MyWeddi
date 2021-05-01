package com.myweddi.webapp;

import com.myweddi.conf.Global;
import com.myweddi.db.ActivationRepository;
import com.myweddi.db.PasswordRestRepository;
import com.myweddi.model.PasswordForm;
import com.myweddi.model.PasswordReset;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    private UserAuthRepository userAuthRepository;
    private PasswordRestRepository passwordRestRepository;
    private PasswordEncoder passwordEncoder;
    private RestTemplate restTemplate;

    @Autowired
    public ForgotPasswordController(UserAuthRepository userAuthRepository, PasswordRestRepository passwordRestRepository, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.passwordRestRepository = passwordRestRepository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String forgotPasswordForm(){
        return "public/password/askfornewpassword";
    }

    @PostMapping
    public String sendLink(@RequestParam("login") String login, RedirectAttributes ra){

        String path = Global.domain + "/api/forgotpassword";
        ResponseEntity<Void> response = null;
        try {
            response = restTemplate.postForEntity(path, login, Void.class);
        }catch (HttpClientErrorException e){
            if(e.getStatusCode().equals(HttpStatus.NOT_FOUND)){
                ra.addAttribute("message", "Taki login/email nie istnieje");
                return "redirect:/login";
            }
            throw new RuntimeException("Unknown exception");

        }

        ra.addAttribute("message", "Aby zmienić hasło kliknij link w emailu");
        return "redirect:/login";
    }

    @GetMapping("/new/{passwordcode}")
    private String resetPassword(@PathVariable("passwordcode") String passwordcode, Model model, RedirectAttributes ra){

        if(!this.passwordRestRepository.existsByPasswordcode(passwordcode)){
            ra.addFlashAttribute("errormessage", "Błędny kod");
            return "redirect:/login";
        }

        PasswordReset passwordCodeObject = this.passwordRestRepository.findByPasswordcode(passwordcode);
        model.addAttribute("passwordForm", new PasswordForm(passwordCodeObject.getUserid()));
        return "public/password/newpassword";
    }

    @PostMapping("/new")
    private String changePassword(PasswordForm passwordForm, Model model, RedirectAttributes ra){

        if(!passwordForm.passwordsAreCorrect()){
            model.addAttribute("passwordForm", passwordForm);
            model.addAttribute("errormessage", "Hasła nie są takie same");
            return "public/password/newpassword";
        }

        Optional<UserAuth> oUser = this.userAuthRepository.findById(passwordForm.getUserid());
        if(oUser.isEmpty()){
            ra.addAttribute("errormessage", "Użytkowniki nie istnieje");
            return "redirect:/login";
        }

        UserAuth userAuth = oUser.get();
        userAuth.setPassword(passwordEncoder.encode(passwordForm.getPass1()));
        this.userAuthRepository.save(userAuth);
        ra.addAttribute("message", "Hasło zostało zmienione");
        return "redirect:/login";
    }



}
