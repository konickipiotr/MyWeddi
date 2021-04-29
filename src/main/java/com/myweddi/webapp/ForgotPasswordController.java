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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private PasswordRestRepository passwordRestRepository;
    private final JavaMailSender javaMailSender;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ForgotPasswordController(UserAuthRepository userAuthRepository, HostRepository hostRepository, PasswordRestRepository passwordRestRepository, JavaMailSender javaMailSender, PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.passwordRestRepository = passwordRestRepository;
        this.javaMailSender = javaMailSender;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String forgotPasswordForm(){
        return "public/askfornewpassword";
    }

    @PostMapping
    public String sendLink(@RequestParam("login") String login, RedirectAttributes ra){

        String emaill = "";
        Long userid;
        if(!this.userAuthRepository.existsByUsername(login)){
            Optional<Host> oBrideemail = this.hostRepository.findByBrideemail(login);
            if(oBrideemail.isEmpty()) {
                Optional<Host> oGroomemail = this.hostRepository.findByGroomemail(login);
                if(oGroomemail.isEmpty()){
                    ra.addAttribute("message", "Taki login/email nie istnieje");
                    return "redirect:/login";
                }else {
                    emaill = oGroomemail.get().getGroomemail();
                    userid = oGroomemail.get().getId();
                }

            }else {
                emaill = oBrideemail.get().getBrideemail();
                userid = oBrideemail.get().getId();
            }
        }else {
            UserAuth userAuth = this.userAuthRepository.findByUsername(login);
            emaill = userAuth.getUsername();
            userid = userAuth.getId();
        }

        String passwordCode = RandomString.make(40);
        this.passwordRestRepository.save(new PasswordReset(userid, passwordCode));
        sendActivationLink(emaill,passwordCode);

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
        return "public/newpassword";
    }

    @PostMapping("/new")
    private String changePassword(PasswordForm passwordForm, Model model, RedirectAttributes ra){

        if(!passwordForm.passwordsAreCorrect()){
            model.addAttribute("passwordForm", passwordForm);
            model.addAttribute("errormessage", "Hasła nie są takie same");
            return "public/newpassword";
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


    private void sendActivationLink(String email, String passwordCode){

        String link = Global.domain + "/forgotpassword/new/" + passwordCode;

        String message = "Witaj w MyWeddi!\n\n" +
                "Kliknij w link poniżej zresetować hasło \n" + link;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject("MyWeddi - nowe hasło");
        emailMessage.setText(message);

        javaMailSender.send(emailMessage);
    }
}
