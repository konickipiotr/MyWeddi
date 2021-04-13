package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.model.TableWrapper;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/guest/tables")
public class GuestTableController {

    private RestTemplate restTemplate;
    private UserAuthRepository userAuthRepository;
    private GuestRepository guestRepository;

    @Autowired
    public GuestTableController(RestTemplate restTemplate, UserAuthRepository userAuthRepository, GuestRepository guestRepository) {
        this.restTemplate = restTemplate;
        this.userAuthRepository = userAuthRepository;
        this.guestRepository = guestRepository;
    }

    @GetMapping
    public String showTables(Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        Guest guest = this.guestRepository.findById(user.getId()).get();
        String path = Global.domain + "/api/table/" + guest.getWeddingid();

        ResponseEntity<TableWrapper> response = restTemplate.getForEntity(path, TableWrapper.class);
        if(response.getStatusCode().is2xxSuccessful()){
            TableWrapper tw = response.getBody();
            model.addAttribute("tables", tw.getTables());
            model.addAttribute("tablesList", tw.getTablePlaces());
            model.addAttribute("defaultMessage", Msg.empty);
        }
        model.addAttribute("menu", Menu.guestMenu);
        return "guest/tables";
    }

    private UserAuth configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }
}
