package com.myweddi.api;

import com.myweddi.db.PhotoRepository;
import com.myweddi.user.Host;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;


@Service
public class UserService {

    private UserAuthRepository userAuthRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private PhotoRepository photoRepository;
    private FileService fileService;

    @Autowired
    public UserService(UserAuthRepository userAuthRepository, HostRepository hostRepository, GuestRepository guestRepository, PhotoRepository photoRepository, FileService fileService) {
        this.userAuthRepository = userAuthRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.photoRepository = photoRepository;
        this.fileService = fileService;
    }

    public ResponseEntity<User> getUserResponseEntity(String username) {
        UserAuth userAuth = userAuthRepository.findByUsername(username);
        if(userAuth == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        User user = getUserResponseEntity(userAuth);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    public User getUser(String username) {
        UserAuth userAuth = userAuthRepository.findByUsername(username);
        if(userAuth == null)
            return null;
        User user = getUserResponseEntity(userAuth);
        return user;
    }

    public ResponseEntity<Host> getWeddingHosts(Long weddingid){
        Host host = hostRepository.findById(weddingid).get();
        return new ResponseEntity<Host>(host, HttpStatus.OK);
    }

    public String getEncodedPhoto(Long userid){
        UserAuth userAuth = userAuthRepository.findById(userid).get();
        User user = getUserResponseEntity(userAuth);

        String photoPath = user.getRealPath();
        String stringBytes = new String();

        if(photoPath != null && !photoPath.isBlank()){
            File file = new File(photoPath);
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                stringBytes = Base64.getEncoder().encodeToString(fileInputStream.readAllBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringBytes;
    }

    public User getUserResponseEntity(UserAuth userAuth){
        User user = null;
        switch (userAuth.getRole()){
            case "GUEST": user = new User(this.guestRepository.findById(userAuth.getId()).get()); break;
            case "HOST": user = new User(this.hostRepository.findById(userAuth.getId()).get()); break;
        }
        user.setRole(userAuth.getRole());
        return user;
    }
}
