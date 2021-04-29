package com.myweddi.webapp.host.info;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.webapp.AuthServiceWebApp;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/host/info")
public class HostWeddingInfoController {

    private final AuthServiceWebApp authServiceWebApp;
    private final String ROOT_PATH = Global.domain + "/api/weddinginfo";

    @Autowired
    public HostWeddingInfoController(AuthServiceWebApp authServiceWebApp) {
        this.authServiceWebApp = authServiceWebApp;
    }

    @GetMapping
    public String getWeddingInfo(Model model, Principal principal){
        RestTemplate restTemplate = authServiceWebApp.configRestTemplate(principal.getName());
        Long weddingId = authServiceWebApp.getWeddingid(principal.getName());
        String path = ROOT_PATH+ "/" + weddingId;
        ResponseEntity<WeddingInfo> response = null;

        try {
            response = restTemplate.getForEntity(path, WeddingInfo.class);
        }catch (HttpClientErrorException e){
            String errormessage = "";
            HttpStatus statusCode = e.getStatusCode();

            if(statusCode.equals(HttpStatus.NOT_FOUND)) errormessage = Msg.notFoundWeddingInfo;
            else if(statusCode.equals(HttpStatus.FORBIDDEN)) errormessage = Msg.forbiddenUser;
            else errormessage = Msg.unknownProblem;
            return  "redirect:/err";
        }

        WeddingInfo weddingInfo = response.getBody();
        model.addAttribute("weddingInfo", weddingInfo);
        model.addAttribute("menu", Menu.hostMenu);
        return "host/info";
    }


    @PostMapping
    public String createUpdateWeddingInfo(@RequestParam("chimage") MultipartFile chimage[],
                                          @RequestParam("wimage") MultipartFile wimage[],
                                          WeddingInfo weddingInfo,
                                          Model model,
                                          Principal principal){

        String path = Global.domain + "/api/weddinginfo";
        RestTemplate restTemplate = authServiceWebApp.configRestTemplate(principal.getName());
        ResponseEntity response = null;
        try {
            response = restTemplate.postForEntity(path, weddingInfo, Void.class);
        }catch (HttpClientErrorException e){

        }

        return "";

    }
//
//    @PostMapping
//    public String saveInfo(@RequestParam("chimage") MultipartFile chimage[], @RequestParam("wimage") MultipartFile wimage[], WeddingInfo weddingInfo, Model model, Principal principal){
//        UserAuth user = configRestTemplate(principal.getName());
//
//        ChurchInfo churchInfo = weddingInfo.getChurchInfo();
//        String path = Global.domain + "/api/churchinfo";
//        ResponseEntity<ChurchInfo> response = restTemplate.postForEntity(path, churchInfo, ChurchInfo.class);
//
//
//        String addFilepath =   Global.domain + "/api/churchinfo/photo/"+ user.getId();
//        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        for (MultipartFile f : chimage) {
//            if (!f.isEmpty()) {
//                try {
//                    body.add("images", new MultipartInputStreamFileResource(f.getInputStream(), f.getOriginalFilename()));
//                } catch (IOException e) {
//                    throw new FailedSaveFileException();
//                }
//            }
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//        if(!body.isEmpty()){
//            restTemplate.postForObject(addFilepath, requestEntity, String.class);
//        }
//
//
//        com.myweddi.model.WeddingInfo weddingInfo = weddingDTO.getWeddingInfo();
//        path = Global.domain + "/api/weddinginfo";
//
//        ResponseEntity<com.myweddi.model.WeddingInfo> response2 = restTemplate.postForEntity(path, weddingInfo, com.myweddi.model.WeddingInfo.class);
//        addFilepath =   Global.domain + "/api/weddinginfo/"+ user.getId() + "/photo";
//        body = new LinkedMultiValueMap<>();
//        for (MultipartFile f : wimage) {
//            if (!f.isEmpty()) {
//                try {
//                    body.add("images", new MultipartInputStreamFileResource(f.getInputStream(), f.getOriginalFilename()));
//                } catch (IOException e) {
//                    throw new FailedSaveFileException();
//                }
//            }
//        }
//
//
//        if(!body.isEmpty()) {
//            requestEntity = new HttpEntity<>(body, headers);
//            restTemplate.postForObject(addFilepath, requestEntity, String.class);
//        }
//
//        return "redirect:/host/info";
//    }
//
//    @PostMapping("/wedding")
//    public String saveWeddingInfo(@RequestParam("image") MultipartFile[] file, com.myweddi.model.WeddingInfo weddingInfo, Model model, Principal principal, RedirectAttributes ra){
//        UserAuth user = userAuthRepository.findByUsername(principal.getName());
//        weddingInfo.setWeddingid(user.getId());
//        String path = Global.domain + "/api/weddinginfo";
//        restTemplate.getInterceptors().clear();
//        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
//        ResponseEntity<com.myweddi.model.WeddingInfo> response = restTemplate.postForEntity(path, weddingInfo, com.myweddi.model.WeddingInfo.class);
//
//        String addFilepath =   "/api/weddinginfo/"+ user.getId() + "/photo";
//        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//        for (MultipartFile f : file) {
//            if (!f.isEmpty()) {
//                try {
//                    body.add("images", new MultipartInputStreamFileResource(f.getInputStream(), f.getOriginalFilename()));
//                } catch (IOException e) {
//                    throw new FailedSaveFileException();
//                }
//            }
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
//        restTemplate.postForObject(addFilepath, requestEntity, String.class);
//
//        return "redirect:/host/info";
//    }
//
//    private UserAuth  configRestTemplate(String username){
//        UserAuth user = userAuthRepository.findByUsername(username);
//        restTemplate.getInterceptors().clear();
//        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        return user;
//    }
}
