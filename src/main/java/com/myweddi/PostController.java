package com.myweddi;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Comment;
import com.myweddi.model.Post;
import com.myweddi.model.Posttype;
import com.myweddi.model.WeddiLike;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.PostView;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/home")
public class PostController {
    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;
    private GuestRepository guestRepository;

    @Autowired
    public PostController(UserAuthRepository userAuthRepository, RestTemplate restTemplate, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
        this.guestRepository = guestRepository;
    }

    @GetMapping
    public String home(Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        Long weddingid;
        if(user.getRole().equals("HOST"))
            weddingid = user.getId();
        else
            weddingid = this.guestRepository.findById(user.getId()).get().getWeddingid();

        String path = Global.domain + "/api/post/" + weddingid + "/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);

        if(response.getStatusCode() == HttpStatus.OK){
            List<PostView> postViews = response.getBody().getList();
            model.addAttribute("posts", postViews);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("posttype", Posttype.LOCAL);
        setMenu(user, model);
        return "home";
    }

    @GetMapping("/public")
    public String homePublic(Model model, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/public/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);

        if(response.getStatusCode() == HttpStatus.OK){
            List<PostView> postViews = response.getBody().getList();
            model.addAttribute("posts", postViews);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("posttype", Posttype.PUBLIC);
        setMenu(user, model);
        return "home";
    }

    @PostMapping
    public String newPost(Post post, MultipartFile[] images,  @RequestParam("posttype") Posttype posttype, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/";
        ResponseEntity<Long> response = restTemplate.postForEntity(path, post, Long.class);

        Long postid = response.getBody();
        path += user.getId() + "/" + postid;

        if(images == null || images.length == 0 || images[0].isEmpty())
            if(posttype.equals(Posttype.LOCAL))
                return "redirect:/home";
            else
                return "redirect:/home/public";

        List<String> sImages = new ArrayList<>();
        for (MultipartFile f : images) {
            if (!f.isEmpty()) {
                try {
                    byte[] bytes = f.getBytes();
                    sImages.add(Base64.getEncoder().encodeToString(bytes));
                } catch (IOException e) {
                    throw new FailedSaveFileException();
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(sImages, headers);
        restTemplate.postForObject(path, requestEntity, String.class);

        if(posttype.equals(Posttype.LOCAL))
            return "redirect:/home";
        else
            return "redirect:/home/public";
    }

    @PostMapping("/addcomment")
    public String addComment(Comment comment, @RequestParam("posttype") Posttype posttype, Principal principal){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/addcomment";
        ResponseEntity<Long> response = restTemplate.postForEntity(path, comment, Long.class);
        if(posttype.equals(Posttype.LOCAL))
            return "redirect:/home";
        else
            return "redirect:/home/public";
    }

    @PostMapping("/star")
    public String star(@RequestParam("postid") Long postid, @RequestParam("posttype") Posttype posttype, Principal principal){
        UserAuth user = configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/changestar";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(path, new WeddiLike(postid, user.getId()), Boolean.class);
        if(posttype.equals(Posttype.LOCAL))
            return "redirect:/home";
        else
            return "redirect:/home/public";
    }

    @PostMapping("/deletepost")
    public String deletePost(@RequestParam("postid") Long postid, @RequestParam("posttype") Posttype posttype, Principal principal){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/deletepost";
        ResponseEntity<Void> response = restTemplate.exchange(path, HttpMethod.DELETE, new HttpEntity<Long>(postid), Void.class);
        if(posttype.equals(Posttype.LOCAL))
            return "redirect:/home";
        else
            return "redirect:/home/public";
    }

    @PostMapping("/deletecomment")
    public String deletecomment(@RequestParam("commentid") Long commentid, @RequestParam("posttype") Posttype posttype, Principal principal){
        configRestTemplate(principal.getName());
        String path = Global.domain + "/api/post/deletecomment";
        ResponseEntity<Void> response = restTemplate.exchange(path, HttpMethod.DELETE, new HttpEntity<Long>(commentid), Void.class);
        if(posttype.equals(Posttype.LOCAL))
            return "redirect:/home";
        else
            return "redirect:/home/public";
    }

    private UserAuth  configRestTemplate(String username){
        UserAuth user = userAuthRepository.findByUsername(username);
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return user;
    }

    private void setMenu(UserAuth user, Model model){
        if(user.getRole().equals("HOST"))
            model.addAttribute("menu", Menu.hostMenu);
        else if(user.getRole().equals("GUEST"))
            model.addAttribute("menu", Menu.guestMenu);
    }
}
