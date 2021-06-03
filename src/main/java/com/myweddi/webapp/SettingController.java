package com.myweddi.webapp;

import com.myweddi.conf.Global;
import com.myweddi.model.PasswordForm;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class SettingController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SettingController(UserAuthRepository userAuthRepository, RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/host/settings")
    public String toHostSettings(Model model, String errpassmessage, String green, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("passPath", "/host/password");
        model.addAttribute("returnView", "HOSTSETTINGS");
        model.addAttribute("errpassmessage", errpassmessage);
        model.addAttribute("green", green);
        model.addAttribute("userid", userAuth.getId());
        model.addAttribute("menu", Menu.hostMenu);
        return "settings";
    }

    @GetMapping("/guest/settings")
    public String toGuestSettings(Model model, String errpassmessage, String green, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/user/guest";
        ResponseEntity<Guest> response = restTemplate.postForEntity(path, userAuth.getId(), Guest.class);
        model.addAttribute("guest", response.getBody());
        model.addAttribute("passwordForm", new PasswordForm());
        model.addAttribute("errpassmessage", errpassmessage);
        model.addAttribute("green", green);
        model.addAttribute("passPath", "/guest/password");
        model.addAttribute("returnView", "GUESTSETTINGS");
        model.addAttribute("userid", userAuth.getId());
        model.addAttribute("menu", Menu.guestMenu);
        return "settings";
    }

    @PostMapping("/host/password")
    public String changeHostPassword(PasswordForm passwordForm, RedirectAttributes model, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());

        if(checkPassword(passwordForm, userAuth, model)) {
            Map<String, String> body = new TreeMap<>();
            body.put("newPassword", passwordForm.getPass1());
            String path = Global.domain + "/api/settings/password";
            restTemplate.postForEntity(path, body, Void.class);
            model.addAttribute("green", "Hasło zostało zmienione");
        }
        return "redirect:/host/settings";
    }


    @PostMapping("/guest/password")
    public String changeGuestPassword(PasswordForm passwordForm, RedirectAttributes model, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());

        if(checkPassword(passwordForm, userAuth, model)) {
            Map<String, String> body = new TreeMap<>();
            body.put("newPassword", passwordForm.getPass1());
            String path = Global.domain + "/api/settings/password";
            restTemplate.postForEntity(path, body, Void.class);
            model.addAttribute("green", "Hasło zostało zmienione");
        }
        return "redirect:/guest/settings";
    }

    private boolean checkPassword(PasswordForm passwordForm, UserAuth userAuth, RedirectAttributes ra){
        boolean result = true;
        String message = "";
        if(passwordEncoder.matches(passwordForm.getPassold(), userAuth.getPassword())){
            message += "Błedne stare hasło";
            result = false;
        }

        if(!passwordForm.passwordsAreCorrect()){
            message += "Nowe hasała nie są takie same";
            result = false;
        }

        if(passwordEncoder.matches(passwordForm.getPass1(), userAuth.getPassword())){
            message += "Nowe hasało musi być inne niż stare";
            result = false;
        }

        if(!result)
            ra.addAttribute("errpassmessage", message);

        return result;
    }



    @PostMapping("/guest/settings/update")
    public String guestUpdateSetting(Guest guest, Model model, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/settings/updateguest";
        restTemplate.postForEntity(path, guest, Guest.class);
        return "redirect:/guest/settings";
    }

    @PostMapping("/host/settings/removeaccount")
    public String removeAccount(@RequestParam("removeaccount") Long weddingid, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/settings/removewedding";
        Map<String, Long> body = new HashMap<>();
        body.put("userid", userAuth.getId());
        body.put("weddingid", weddingid);
        try {
            restTemplate.postForEntity(path, body, Void.class);
        }catch (HttpClientErrorException e){

        }
        return "redirect:/logout";
    }

    @PostMapping("/guest/settings/removeguest")
    public String removeGuestAccount(@RequestParam("removeaccount") Long userid, Principal principal){
        UserAuth userAuth = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/settings/removeguest";
        Map<String, Long> body = new HashMap<>();
        body.put("userid", userid);
        try {
            restTemplate.postForEntity(path, body, Void.class);

        }catch (HttpClientErrorException e){

        }
        return "redirect:/logout";
    }

    private UserAuth configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }


}
