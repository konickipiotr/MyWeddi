package com.myweddi.api.guest;

import com.myweddi.user.*;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.OneTimeRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.ListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GuestService {

    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private PasswordEncoder passwordEncoder;
    private OneTimeRepository oneTimeRepository;
    private Random random = new Random();

    @Autowired
    public GuestService(GuestRepository guestRepository, UserAuthRepository userAuthRepository,
                        HostRepository hostRepository, PasswordEncoder passwordEncoder,
                        OneTimeRepository oneTimeRepository) {
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.passwordEncoder = passwordEncoder;
        this.oneTimeRepository = oneTimeRepository;
    }

    public ListWrapper<Guest> getGuestList(Long weddingid){
        List<Guest> guestList = this.guestRepository.findByWeddingid(weddingid);
        guestList.forEach(i -> i.setIco());
        return new ListWrapper(guestList);
    }

    public void addGuest(Guest guest){
        String passowrd = generateOneTimePassword();
        UserAuth userAuth = new UserAuth();
        userAuth.setRole("GUEST");
        userAuth.setUsername(getUsername(guest));
        userAuth.setPassword(passwordEncoder.encode(passowrd));
        //userAuth.setStatus(UserStatus.FIRSTLOGIN);
        userAuth.setStatus(UserStatus.ACTIVE); // TODO temporary
        this.userAuthRepository.save(userAuth);

        guest.setId(userAuth.getId());
        guest.setRole("GUEST");
        guest.setStatus(GuestStatus.NOTCONFIRMED);
        this.guestRepository.save(guest);

        this.oneTimeRepository.save(new OneTime(userAuth.getId(), passowrd));
    }

    private String getUsername(Guest guest){
        StringBuilder username = new StringBuilder();
        String firstname = guest.getFirstname();
        String lastname = guest.getLastname();

        username.append(lastname.toLowerCase() + firstname.toLowerCase());
        if(this.userAuthRepository.existsByUsername(username.toString())){
            Host host = this.hostRepository.findById(guest.getWeddingid()).get();
            username = new StringBuilder();
            username.append(host.getBridefirstname().charAt(0));
            username.append(host.getBridelastname().charAt(0));
            username.append(host.getGroomfirstname().charAt(0));
            username.append(host.getGroomlastname().charAt(0));
            username.append(lastname.toLowerCase() + firstname.toLowerCase());

            while (this.userAuthRepository.existsByUsername(username.toString())){
                username.append(random.nextInt(9));
            }
        }
        return  username.toString();
    }

    private String generateOneTimePassword(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++){
            int type = random.nextInt(3);
            int val = 0;
            switch (type){
                case 0: val = random.nextInt(10) + 48; break;
                case 1: val = random.nextInt(26) + 65; break;
                case 2: val = random.nextInt(26) + 97; break;
            }
            char c = (char) val;
            sb.append(c);
        }
        return sb.toString();
    }
}
