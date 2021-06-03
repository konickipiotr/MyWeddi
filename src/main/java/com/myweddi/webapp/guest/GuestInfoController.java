package com.myweddi.webapp.guest;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.webapp.AuthServiceWebApp;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/info")
public class GuestInfoController {

    private final AuthServiceWebApp authServiceWebApp;

    @Autowired
    public GuestInfoController(AuthServiceWebApp authServiceWebApp) {
        this.authServiceWebApp = authServiceWebApp;
    }

    @GetMapping
    public String getWeddingInfo(Model model, Principal principal, RedirectAttributes ra){
        RestTemplate restTemplate = authServiceWebApp.configRestTemplate(principal.getName());
        Long weddingId = authServiceWebApp.getWeddingid(principal.getName());
        String path = Global.domain + "/api/weddinginfo/" + weddingId;
        ResponseEntity<WeddingInfo> response;

        try {
            response = restTemplate.getForEntity(path, WeddingInfo.class);
        }catch (HttpClientErrorException e){
            String errormessage;
            HttpStatus statusCode = e.getStatusCode();

            if(statusCode.equals(HttpStatus.NOT_FOUND)) errormessage = Msg.notFoundWeddingInfo;
            else if(statusCode.equals(HttpStatus.FORBIDDEN)) errormessage = Msg.forbiddenUser;
            else errormessage = Msg.unknownProblem;

            ra.addAttribute("errormessage", errormessage);
            return  "redirect:/err";
        }

        WeddingInfo weddingInfo = response.getBody();


        weddingInfo.convertDate();
        model.addAttribute("weddingInfo", weddingInfo);
        model.addAttribute("menu", Menu.guestMenu);
        return "guest/infoview";
    }
}
