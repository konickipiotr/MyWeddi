package com.myweddi.webapp.host;

import com.myweddi.conf.Global;
import com.myweddi.model.Post;
import com.myweddi.model.Posttype;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.PostView;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/host")
public class HostHomeController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public HostHomeController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String home(Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());

        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        String path = Global.domain + "/api/post/" + user.getId() + "/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);

        if(response.getStatusCode() == HttpStatus.OK){
            List<PostView> postViews = response.getBody().getList();
            model.addAttribute("posts", postViews);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("postType", Posttype.LOCAL);
        model.addAttribute("menu", Menu.hostMenu);
        return "host/home";
    }

    @GetMapping("/public")
    public String homePublic(Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());

        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        String path = Global.domain + "/api/post/public/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);

        if(response.getStatusCode() == HttpStatus.OK){
            List<PostView> postViews = response.getBody().getList();
            model.addAttribute("posts", postViews);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("postType", Posttype.PUBLIC);
        model.addAttribute("menu", Menu.hostMenu);
        return "host/home";
    }
}
