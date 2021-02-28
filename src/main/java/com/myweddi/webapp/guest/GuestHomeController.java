package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.ChurchInfo;
import com.myweddi.model.Comment;
import com.myweddi.model.Post;
import com.myweddi.user.Guest;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import com.myweddi.utils.MultipartInputStreamFileResource;
import com.myweddi.view.PostView;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
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
    public String home(Model model, Principal principal){

        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Guest guest = guestRepository.findById(user.getId()).get();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));

        String path = Global.domain + "/api/post/" + guest.getWeddingid() + "/1";
        ResponseEntity<ListWrapper> response = restTemplate.getForEntity(path, ListWrapper.class);

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

        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile f : images) {
            if (!f.isEmpty()) {
                try {
                    body.add("images", new MultipartInputStreamFileResource(f.getInputStream(), f.getOriginalFilename()));
                } catch (IOException e) {
                    throw new FailedSaveFileException();
                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
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
}
