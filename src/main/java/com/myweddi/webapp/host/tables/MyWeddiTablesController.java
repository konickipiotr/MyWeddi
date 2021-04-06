package com.myweddi.webapp.host.tables;

import com.myweddi.conf.Global;
import com.myweddi.model.ChurchInfo;
import com.myweddi.model.TableTempObject;
import com.myweddi.model.TableWrapper;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Controller
@RequestMapping("/host/tables")
public class MyWeddiTablesController {

    private UserAuthRepository userAuthRepository;
    private RestTemplate restTemplate;

    @Autowired
    public MyWeddiTablesController(UserAuthRepository userAuthRepository, RestTemplate restTemplate) {
        this.userAuthRepository = userAuthRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String tables(Principal principal, Model model){

        UserAuth userAuth = userAuthRepository.findByUsername(principal.getName());
        String path = Global.domain + "/api/table/" + userAuth.getId();
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userAuth.getUsername(), userAuth.getPassword()));
        ResponseEntity<TableWrapper> response = restTemplate.getForEntity(path, TableWrapper.class);
        if(response.getStatusCode().is2xxSuccessful()){
            TableWrapper tw = response.getBody();
            model.addAttribute("tables", tw.getTables());
            model.addAttribute("tablesList", tw.getTablePlaces());
            model.addAttribute("assigned", tw.getAssigned());
            model.addAttribute("notassigned", tw.getNotassigned());
        }

        model.addAttribute("weddingid", userAuth.getId());
        return "host/tables";

    }

    @GetMapping("/settables")
    public String setTables(Principal principal, Model model){

        UserAuth userAuth = userAuthRepository.findByUsername(principal.getName());
        model.addAttribute("weddingid", userAuth.getId());
        return "host/settables";
    }

    @PostMapping("/settables")
    public String setTables(@RequestParam("weddingid") Long weddingid,
                         @RequestParam("numoftables") int numoftables,
                         Model model){

        model.addAttribute("numoftables", numoftables);
        model.addAttribute("weddingid", weddingid);
        return "host/settable";
    }

    @PostMapping("/add")
    public String finalizeTable(@RequestParam("weddingid") Long weddingid,
                                int tableid[], int capacity[], Principal principal){

        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        String path = Global.domain + "/api/table";
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));

        TableTempObject tto = new TableTempObject(tableid, capacity, weddingid, user.getId());
        ResponseEntity response = restTemplate.postForEntity(path, tto, Void.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            System.err.println("cannot create tables");
        }

        return "redirect:/host/tables";
    }
}
