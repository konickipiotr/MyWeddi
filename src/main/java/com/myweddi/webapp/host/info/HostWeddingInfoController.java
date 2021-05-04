package com.myweddi.webapp.host.info;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.utils.PhotoCat;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

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
    public String getWeddingInfo(Model model, Principal principal, String errorMessage, RedirectAttributes ra){
        RestTemplate restTemplate = authServiceWebApp.configRestTemplate(principal.getName());
        Long weddingId = authServiceWebApp.getWeddingid(principal.getName());
        String path = ROOT_PATH+ "/" + weddingId;
        ResponseEntity<WeddingInfo> response;

        try {
            response = restTemplate.getForEntity(path, WeddingInfo.class);
        }catch (HttpClientErrorException e){
            HttpStatus statusCode = e.getStatusCode();

            if(statusCode.equals(HttpStatus.NOT_FOUND)) errorMessage += Msg.notFoundWeddingInfo;
            else if(statusCode.equals(HttpStatus.FORBIDDEN)) errorMessage += Msg.forbiddenUser;
            else errorMessage += Msg.unknownProblem;

            ra.addAttribute("errormessage", errorMessage);
            return  "redirect:/err";
        }

        WeddingInfo weddingInfo = response.getBody();
        model.addAttribute("weddingInfo", weddingInfo);
        model.addAttribute("menu", Menu.hostMenu);
        model.addAttribute("errorMessage", errorMessage);
        return "host/info";
    }

    @PostMapping
    public String createUpdateWeddingInfo(@RequestParam("chimage") MultipartFile chimage,
                                          @RequestParam("wimage") MultipartFile wimage,
                                          WeddingInfo weddingInfo,
                                          Model model,
                                          RedirectAttributes ra,
                                          Principal principal){
        String errorMessage = "";
        RestTemplate restTemplate = authServiceWebApp.configRestTemplate(principal.getName());
        Long weddingId = authServiceWebApp.getWeddingid(principal.getName());

        try {
            restTemplate.postForEntity(ROOT_PATH, weddingInfo, Void.class);
        }catch (HttpClientErrorException e){
            ra.addAttribute("errormessage", Msg.unknownProblem);
            return  "redirect:/err";
        }

        errorMessage += uploadPhoto(chimage, weddingId, PhotoCat.CHURCH, restTemplate);
        errorMessage += uploadPhoto(wimage, weddingId, PhotoCat.WEDDINGHOUSE, restTemplate);
        ra.addAttribute("errorMessage", errorMessage);
        return "redirect:/host/info";
    }

    private String uploadPhoto(MultipartFile file, Long weddingId, PhotoCat photoCat, RestTemplate restTemplate) {
        String path = ROOT_PATH + "/" + weddingId;
        String errorMessage = "";
        String potentialErrorMsg;
        if(photoCat.equals(PhotoCat.CHURCH)) {
            path += "/churchphoto";
            potentialErrorMsg = Msg.couldNotUploadChurchPhoto;
        }
        else {
            path += "/weddinghousephoto";
            potentialErrorMsg = Msg.couldNotUploadWeddingHousePhoto;
        }

        String stringByteImage;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            if(!file.isEmpty()){
                stringByteImage = Base64.getEncoder().encodeToString(file.getBytes());
                restTemplate.postForObject(path, new HttpEntity<>(stringByteImage, headers), String.class);
            }
        } catch (IOException | HttpClientErrorException e) {
            errorMessage = potentialErrorMsg;
        }
        return errorMessage;
    }
}
