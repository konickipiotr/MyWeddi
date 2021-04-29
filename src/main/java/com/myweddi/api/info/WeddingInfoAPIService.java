package com.myweddi.api.info;

import com.myweddi.api.UserService;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.user.User;
import com.myweddi.utils.AccessUtil;
import com.myweddi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
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

    public ResponseEntity addWeddingInfo(WeddingInfo weddingInfo, String username){

        User user = userService.getUser(username);
        if(AccessUtil.writeAccessDenied(user, weddingInfo.getWeddingid()))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        if(this.weddingInfoRepository.existsById(weddingInfo.getWeddingid())){
            WeddingInfo weddingInfoDB = this.weddingInfoRepository.findById(weddingInfo.getWeddingid()).get();
            weddingInfoDB.update(weddingInfo);
            this.weddingInfoRepository.save(weddingInfoDB);
            return new ResponseEntity(HttpStatus.FOUND);

        }else {
            this.weddingInfoRepository.save(weddingInfo);
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }

    public ResponseEntity<WeddingInfo> getWeddingInfo(Long weddingid, String username){
        User user = userService.getUser(username);
        if(AccessUtil.accessDenied(user, weddingid))
            return new ResponseEntity(HttpStatus.FORBIDDEN);

        Optional<WeddingInfo> oWeddingInfo = this.weddingInfoRepository.findById(weddingid);
        if(oWeddingInfo.isEmpty())
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        return new ResponseEntity<WeddingInfo>(oWeddingInfo.get(), HttpStatus.OK);
    }

}
