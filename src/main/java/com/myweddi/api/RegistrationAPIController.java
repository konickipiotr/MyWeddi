package com.myweddi.api;

import com.myweddi.user.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.user.reposiotry.WeddingCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Random;


@RestController
@RequestMapping("/api/registration")
public class RegistrationAPIController {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private WeddingCodeRepository weddingCodeRepository;
    private GuestRepository guestRepository;

    @Autowired
    public RegistrationAPIController(UserAuthRepository userAuthRepository, HostRepository hostRepository, WeddingCodeRepository weddingCodeRepository, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.weddingCodeRepository = weddingCodeRepository;
        this.guestRepository = guestRepository;
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

        this.weddingCodeRepository.save(new WeddingCode(userAuth.getId(), generateWeddingCode()));
        return new ResponseEntity<RegistrationForm>(HttpStatus.OK);
    }

    @PostMapping("/guest")
    public ResponseEntity<Boolean> addGuest( @RequestBody RegistrationForm rf){
        UserAuth userAuth = this.userAuthRepository.findByUsername(rf.getUsername());

        if(userAuth != null){
            return  new ResponseEntity<Boolean>(false, HttpStatus.FOUND);
        }

        if(!this.weddingCodeRepository.existsByWeddingcode(rf.getWeddingcode())){
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_FOUND);
        }
        WeddingCode weddingCode = this.weddingCodeRepository.findByWeddingcode(rf.getWeddingcode());

        userAuth = new UserAuth(rf);
        this.userAuthRepository.save(userAuth);

        this.guestRepository.save(new Guest(userAuth.getId(), weddingCode.getWeddingid(), userAuth.getUsername(), rf.getFirstname(), rf.getLastname()));
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    private String generateWeddingCode(){
        byte[] array = new byte[8];

        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        while (this.weddingCodeRepository.existsByWeddingcode(generatedString)){
            new Random().nextBytes(array);
            generatedString = new String(array, Charset.forName("UTF-8"));
        }
        return generatedString;
    }

}
