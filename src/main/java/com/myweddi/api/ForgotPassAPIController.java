package com.myweddi.api;

import com.myweddi.conf.Global;
import com.myweddi.db.PasswordRestRepository;
import com.myweddi.model.PasswordReset;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import net.bytebuddy.utility.RandomString;
import org.hibernate.internal.build.AllowPrintStacktrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@RestController
@RequestMapping("/api/forgotpassword")
public class ForgotPassAPIController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private JavaMailSender javaMailSender;
    private PasswordRestRepository passwordRestRepository;

    @Autowired
    public ForgotPassAPIController(UserAuthRepository userAuthRepository, HostRepository hostRepository, JavaMailSender javaMailSender, PasswordRestRepository passwordRestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.javaMailSender = javaMailSender;
        this.passwordRestRepository = passwordRestRepository;
    }

    @PostMapping
    public ResponseEntity sendLink(@RequestBody String login){

        if(login.startsWith("\""))
            login = login.replaceAll("\"", "");

        String emaill = "";
        String email2 = "";
        Long userid;
        if(!this.userAuthRepository.existsByUsername(login)){
            Optional<Host> oBrideemail = this.hostRepository.findByBrideemail(login);
            if(oBrideemail.isEmpty()) {
                Optional<Host> oGroomemail = this.hostRepository.findByGroomemail(login);
                if(oGroomemail.isEmpty()){
                    return new ResponseEntity(HttpStatus.NOT_FOUND);
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
            if(userAuth.getRole().equals("HOST")){
                Optional<Host> oHost = this.hostRepository.findById(userAuth.getId());
                if(oHost.isEmpty())
                    return new ResponseEntity(HttpStatus.NOT_FOUND);

                Host host = oHost.get();
                emaill = host.getBrideemail();
                email2 = host.getGroomemail();
                userid = host.getId();
            }else {
                emaill = userAuth.getUsername();
                userid = userAuth.getId();
            }
        }

        String passwordCode = RandomString.make(40);
        this.passwordRestRepository.save(new PasswordReset(userid, passwordCode));
        sendActivationLink(emaill,passwordCode);
        if(!email2.isBlank())
            sendActivationLink(email2,passwordCode);

        return new ResponseEntity(HttpStatus.OK);
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
