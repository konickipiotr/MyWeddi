package com.myweddi.webapp.host.info;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.info.ChurchInfo;
import com.myweddi.info.WeddingInfo;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.MultipartInputStreamFileResource;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/host/info")
public class HostWeddingInfoController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public HostWeddingInfoController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String toInfoFrom(Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());

        String path = Global.domain + "/api/churchinfo/" + user.getId();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<ChurchInfo> response = restTemplate.getForEntity(path, ChurchInfo.class);

        path = Global.domain + "/api/weddinginfo/" + user.getId();
        ResponseEntity<WeddingInfo> response2 = restTemplate.getForEntity(path, WeddingInfo.class);
        if(response.getStatusCode() == HttpStatus.OK){
            ChurchInfo churchInfo = response.getBody();
            model.addAttribute("churchInfo", churchInfo);
        }else {
            model.addAttribute("churchInfo", new ChurchInfo(user.getId()));
        }

        if(response2.getStatusCode() == HttpStatus.OK){
            WeddingInfo weddingInfo = response2.getBody();
            model.addAttribute("weddingInfo", weddingInfo);
        }else {
            model.addAttribute("weddingInfo", new WeddingInfo(user.getId()));
        }

        model.addAttribute("menu", Menu.hostMenu);
        return "host/info";
    }

    @PostMapping("/church")
    public String saveChurchInfo(@RequestParam("image") MultipartFile[] file, ChurchInfo churchInfo, Model model, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        churchInfo.setWeddingid(user.getId());
        String path = Global.domain + "/api/churchinfo";
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<ChurchInfo> response = restTemplate.postForEntity(path, churchInfo, ChurchInfo.class);


        String addFilepath =   Global.domain + "/api/churchinfo/photo/"+ user.getId();
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile f : file) {
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
        restTemplate.postForObject(addFilepath, requestEntity, String.class);


        return "redirect:/host/info";
    }

    @PostMapping("/wedding")
    public String saveWeddingInfo(@RequestParam("image") MultipartFile[] file, WeddingInfo weddingInfo, Model model, Principal principal, RedirectAttributes ra){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        weddingInfo.setWeddingid(user.getId());
        String path = Global.domain + "/api/weddinginfo";
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity<WeddingInfo> response = restTemplate.postForEntity(path, weddingInfo, WeddingInfo.class);

        String addFilepath =   "/api/weddinginfo/"+ user.getId() + "/photo";
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for (MultipartFile f : file) {
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
        restTemplate.postForObject(addFilepath, requestEntity, String.class);

        return "redirect:/host/info";
    }
}
