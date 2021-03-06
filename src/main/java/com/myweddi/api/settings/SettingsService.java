package com.myweddi.api.settings;

import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import org.intellij.lang.annotations.JdkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingsService {

    private UserAuthRepository userAuthRepository;
    private PasswordEncoder passwordEncoder;
    private GuestRepository guestRepository;

    @Autowired
    public SettingsService(UserAuthRepository userAuthRepository, PasswordEncoder passwordEncoder, GuestRepository guestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.passwordEncoder = passwordEncoder;
        this.guestRepository = guestRepository;
    }

    public ResponseEntity<Void> changePassword(String password, String username){
        UserAuth user = userAuthRepository.findByUsername(username);

        if(password == null | password.isBlank())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        if(user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        user.setPassword(passwordEncoder.encode(password));
        this.userAuthRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updateGuest(Guest guest, String username) {
        UserAuth user = userAuthRepository.findByUsername(username);

        Optional<Guest> oGuest = this.guestRepository.findById(user.getId());
        if(oGuest.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Guest guestOld = oGuest.get();
        guestOld.updateAdditionalInfo(guest);
        this.guestRepository.save(guestOld);
        return new ResponseEntity<>(HttpStatus.OK);

    }

}
