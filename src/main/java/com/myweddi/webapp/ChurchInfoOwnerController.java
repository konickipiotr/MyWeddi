package com.myweddi.webapp;

import com.myweddi.conf.Global;
import com.myweddi.info.ChurchInfo;
import com.myweddi.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/owner/churchinfo")
public class ChurchInfoOwnerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{weddingid}")
    public String getChurchInfo(@PathVariable("weddingid") Long weddingid, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        String url = Global.domain + "/api/churchinfo";
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));

        ResponseEntity<ChurchInfo> response = restTemplate.getForEntity(url, ChurchInfo.class, weddingid);
        if(response.getStatusCode().is2xxSuccessful()){
            ChurchInfo churchInfo = response.getBody();
            model.addAttribute("churchInfo", churchInfo);
        }
        return  "owner/info";
    }
}
