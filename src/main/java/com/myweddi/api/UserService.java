package com.myweddi.api;

import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;

    @Autowired
    public UserService(UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
    }

    public ResponseEntity<User> getUser(String username) {
        UserAuth userAuth = userAuthRepository.findByUsername(username);
        if(userAuth == null)
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);

        User user = null;
        switch (userAuth.getRole()){
            case "GUEST": user = new User(this.guestRepository.findById(userAuth.getId()).get()); break;
            case "HOST": user = new User(this.hostRepository.findById(userAuth.getId()).get()); break;
        }

        return new ResponseEntity<User>(user, HttpStatus.OK);
    }



}
