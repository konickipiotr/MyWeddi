package com.myweddi.api.info;

import com.myweddi.api.UserService;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.user.User;
import com.myweddi.utils.AccessUtil;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class WeddingInfoAPIService {

    private final WeddingInfoRepository weddingInfoRepository;
    private final FileService fileService;
    private final UserService userService;

    @Autowired
    public WeddingInfoAPIService(WeddingInfoRepository weddingInfoRepository, FileService fileService, UserService userService) {
        this.weddingInfoRepository = weddingInfoRepository;
        this.fileService = fileService;
        this.userService = userService;
    }

    public ResponseEntity<Long> addWeddingInfo(WeddingInfo weddingInfo, String username){

        User user = userService.getUser(username);
        if(AccessUtil.writeAccessDenied(user, weddingInfo.getWeddingid()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Optional<WeddingInfo> oWeddingInfo = this.weddingInfoRepository.findById(weddingInfo.getWeddingid());
        if(oWeddingInfo.isPresent()){
            WeddingInfo weddingInfoDB = oWeddingInfo.get();
            weddingInfoDB.update(weddingInfo);
            this.weddingInfoRepository.save(weddingInfoDB);
            return new ResponseEntity<>(weddingInfoDB.getWeddingid(), HttpStatus.FOUND);

        }else {
            this.weddingInfoRepository.save(weddingInfo);
            return new ResponseEntity<>(weddingInfo.getWeddingid(), HttpStatus.CREATED);
        }
    }

    public ResponseEntity<WeddingInfo> getWeddingInfo(Long weddingId, String username){
        User user = userService.getUser(username);
        if(AccessUtil.accessDenied(user, weddingId))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        Optional<WeddingInfo> oWeddingInfo = this.weddingInfoRepository.findById(weddingId);
        if(oWeddingInfo.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(oWeddingInfo.get(), HttpStatus.OK);
    }

    public ResponseEntity<Void> saveWeddingInfoPhoto(Long weddingId, String photo, PhotoCat photoCat){
        if(!photoCat.equals(PhotoCat.CHURCH) && !photoCat.equals(PhotoCat.WEDDINGHOUSE))
            throw  new IllegalArgumentException();

        Optional<WeddingInfo> oWeddingInfo = this.weddingInfoRepository.findById(weddingId);
        if(oWeddingInfo.isEmpty())
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        WeddingInfo weddingInfo = oWeddingInfo.get();

        MultipartFile multipartFile = fileService.convertToMultipartFile(photo);
        FileNameStruct fileNameStruct = fileService.uploadPhoto(multipartFile, photoCat);
        if(fileNameStruct == null)
            throw new FailedSaveFileException();

        removeOldFile(weddingInfo, photoCat);
        savePhotoPathToDB(weddingInfo, fileNameStruct, photoCat);
        return  new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void savePhotoPathToDB(WeddingInfo weddingInfo, FileNameStruct fileNames, PhotoCat photoCat){
        if(photoCat.equals(PhotoCat.CHURCH)){
            weddingInfo.setChWebAppPath(fileNames.webAppPath);
            weddingInfo.setChRealPath(fileNames.realPath);
        }else {
            weddingInfo.setwWebAppPath(fileNames.webAppPath);
            weddingInfo.setwRealPath(fileNames.realPath);
        }
        this.weddingInfoRepository.save(weddingInfo);
    }

    private void removeOldFile(WeddingInfo weddingInfo, PhotoCat photoCat){
        String path = photoCat.equals(PhotoCat.CHURCH) ? weddingInfo.getChRealPath() : weddingInfo.getwRealPath();
        if(path != null && !path.isBlank()) {
            try {
                fileService.deleteFile(path);
            } catch (IOException e) {
                System.err.println("file not exist");
                e.printStackTrace();
            }
        }
    }
}
