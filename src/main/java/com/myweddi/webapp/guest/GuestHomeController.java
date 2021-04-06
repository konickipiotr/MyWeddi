package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Comment;
import com.myweddi.model.Like;
import com.myweddi.model.Post;
import com.myweddi.model.PostUserId;
import com.myweddi.user.Guest;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@RequestMapping("/guest")
public class GuestHomeController {

    private RestTemplate restTemplate;
    private UserAuthRepository userAuthRepository;
    private GuestRepository guestRepository;

    @Autowired
    public GuestHomeController(RestTemplate restTemplate, UserAuthRepository userAuthRepository, GuestRepository guestRepository) {
        this.restTemplate = restTemplate;
        this.userAuthRepository = userAuthRepository;
        this.guestRepository = guestRepository;
    }

    @GetMapping
    public String home(Model model, Principal principal) throws IOException, InterruptedException {

        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        String path = Global.domain + "/api/post/" + guest.getWeddingid() + "/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);
        System.out.println(response);
        if(response.getStatusCode() == HttpStatus.OK){
            List<PostView> postViews = response.getBody().getList();
            model.addAttribute("posts", postViews);
        }
        model.addAttribute("post", new Post());
        model.addAttribute("menu", Menu.guestMenu);
        return "guest/home";
    }

    @PostMapping
    public String newPost(Post post, MultipartFile[] images,  Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        post.setUserid(guest.getId());
        post.setWeddingid(guest.getWeddingid());
        post.setCreationdate(LocalDateTime.now());


        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));

        String path = Global.domain + "/api/post/";
        ResponseEntity<Long> response = restTemplate.postForEntity(path, post, Long.class);


        Long postid = response.getBody();
        path += user.getId() + "/" + postid;

        if(images == null || images.length == 0)
            return "redirect:/guest";

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

        return "redirect:/guest";
    }

    @PostMapping("/addcomment")
    public String addComment(Comment comment, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        comment.setUserid(guest.getId());
        comment.setCreationdate(LocalDateTime.now());


        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));

        String path = Global.domain + "/api/post/addcomment";
        ResponseEntity<Long> response = restTemplate.postForEntity(path, comment, Long.class);
        return "redirect:/guest";
    }

    @PostMapping("/star")
    public String star(@RequestParam("postid") Long postid, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        String path = Global.domain + "/api/post/changestar";
        ResponseEntity<Boolean> response = restTemplate.postForEntity(path, new Like(postid, user.getId()), Boolean.class);


        return "redirect:/guest";
    }
}
