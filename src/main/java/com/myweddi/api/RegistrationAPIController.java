package com.myweddi.api;

import com.myweddi.user.Host;
import com.myweddi.user.RegistrationForm;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/registration")
public class RegistrationAPIController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;

    @Autowired
    public RegistrationAPIController(UserAuthRepository userAuthRepository, HostRepository hostRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
    }

    @PostMapping
    public ResponseEntity addHost( @RequestBody RegistrationForm rf){
        UserAuth userAuth = this.userAuthRepository.findByUsername(rf.getUsername());

        if(userAuth != null){
            return  new ResponseEntity<RegistrationForm>(HttpStatus.FOUND);
        }
        userAuth = new UserAuth(rf);
        userAuth  = this.userAuthRepository.save(userAuth);
        this.hostRepository.save(new Host(userAuth.getId(), rf));
        return new ResponseEntity<RegistrationForm>(HttpStatus.OK);
    }

}
