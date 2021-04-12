package com.myweddi.api;

import com.myweddi.db.PhotoRepository;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Photo;
import com.myweddi.user.Guest;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profilephoto")
public class ProfilePhotoAPIController {

    private FileService fileService;
    private UserAuthRepository userAuthRepository;
    private GuestRepository guestRepository;
    private HostRepository hostRepository;

    @Autowired
    public ProfilePhotoAPIController(FileService fileService, UserAuthRepository userAuthRepository, GuestRepository guestRepository, HostRepository hostRepository) {
        this.fileService = fileService;
        this.userAuthRepository = userAuthRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
    }

    @PostMapping(path = "/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity uploadProfilePhoto(@PathVariable("userid") Long userid, @RequestBody String sImage ){
            List<MultipartFile> multipartFiles = fileService.convertToMultipartFiles(Arrays.asList(sImage));
            List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(multipartFiles, PhotoCat.PROFILE);
            if (fileNameStructs == null || fileNameStructs.isEmpty())
                throw new FailedSaveFileException();

        UserAuth user = this.userAuthRepository.findById(userid).get();
        if(user.getRole().equals("HOST")){
            Host host = this.hostRepository.findById(user.getId()).get();
            host.setRealPath(fileNameStructs.get(0).realPath);
            host.setWebAppPath(fileNameStructs.get(0).webAppPath);
            this.hostRepository.save(host);
        }else if(user.getRole().equals("GUEST")){
            Guest guest = this.guestRepository.findById(user.getId()).get();
            guest.setRealPath(fileNameStructs.get(0).realPath);
            guest.setWebAppPath(fileNameStructs.get(0).webAppPath);
            this.guestRepository.save(guest);
        }
        return new ResponseEntity (HttpStatus.CREATED);
    }
}
