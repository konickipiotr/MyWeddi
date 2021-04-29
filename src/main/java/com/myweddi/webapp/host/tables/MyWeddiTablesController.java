package com.myweddi.webapp.host.tables;

import com.myweddi.conf.Global;
import com.myweddi.conf.Msg;
import com.myweddi.model.TableTempObject;
import com.myweddi.model.TableWrapper;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.webapp.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

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
            model.addAttribute("defaultMessage", Msg.empty);
        }

        model.addAttribute("weddingid", userAuth.getId());
        model.addAttribute("menu", Menu.hostMenu);
        return "host/tables";

    }

    @GetMapping("/settables")
    public String setTables(Principal principal, Model model){

        UserAuth userAuth = userAuthRepository.findByUsername(principal.getName());
        model.addAttribute("weddingid", userAuth.getId());
        model.addAttribute("menu", Menu.hostMenu);
        return "host/settables";
    }

    @PostMapping("/settables")
    public String setTables(@RequestParam("weddingid") Long weddingid,
                         @RequestParam("numoftables") int numoftables,
                         Model model){

        model.addAttribute("numoftables", numoftables);
        model.addAttribute("weddingid", weddingid);
        model.addAttribute("menu", Menu.hostMenu);
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

    @PostMapping("/setguests")
    public String setGuests(Long tableplace[], String vVal[], Principal principal){

        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        String path = Global.domain + "/api/table/setguests";
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        ResponseEntity response = restTemplate.postForEntity(path, vVal, Void.class);
        return "redirect:/host/tables";
    }

    @PostMapping("/loadtableschema")
    public String loadTableSchema(MultipartFile image, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        restTemplate.getInterceptors().clear();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(user.getUsername(), user.getPassword()));
        String path = Global.domain + "/api/table/loadschema";
        byte[] bytes = new byte[0];
        try {
            bytes = image.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringByteImage = Base64.getEncoder().encodeToString(bytes);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(stringByteImage, headers);
        restTemplate.postForObject(path, requestEntity, String.class);

        return "redirect:/host/tables";
    }
}
