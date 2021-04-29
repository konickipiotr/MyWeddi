package com.myweddi.api.info;

import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/weddinginfo")
public class WeddingInfoAPIController {

    private WeddingInfoAPIService weddingInfoAPIService;

    @Autowired
    public WeddingInfoAPIController(WeddingInfoAPIService weddingInfoAPIService) {
        this.weddingInfoAPIService = weddingInfoAPIService;
    }

    @PostMapping
    public ResponseEntity addWeddingInfo(@RequestBody WeddingInfo weddingInfo, Principal principal){
        return weddingInfoAPIService.addWeddingInfo(weddingInfo, principal.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingInfo> getWeddingInfo(@PathVariable("id") Long weddingid, Principal principal){
        return weddingInfoAPIService.getWeddingInfo(weddingid, principal.getName());
    }

//    @DeleteMapping
//    public void deleteChurchInfo(@RequestBody Long weddingid){
//        this.weddingInfoRepository.deleteById(weddingid);
//    }
//
//    @PostMapping("/{weddingid}/photo")
//    public ResponseEntity saveChurchImg(@PathVariable("weddingid") Long weddingid, MultipartFile[] images){ //TODO byteAsString
//        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
//        if(oWedding.isEmpty()){
//            //TODO
//        }
//        WeddingInfo weddingInfo = oWedding.get();
//
//        List<MultipartFile> mList = new ArrayList<>(Arrays.asList(images));
//
//        List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(mList, PhotoCat.WEDDINGHOUSE);
//        if(fileNameStructs == null || fileNameStructs.isEmpty())
//            throw new FailedSaveFileException();
//
//        if(weddingInfo.getRealPath() != null && !weddingInfo.getRealPath().isBlank()) {
//            try {
//                fileService.deleteFile(weddingInfo.getRealPath());
//            } catch (IOException e) {
//                System.err.println("file not exist");
//                e.printStackTrace();
//            }
//        }
//        weddingInfo.setRealPath(fileNameStructs.get(0).realPath);
//        weddingInfo.setWebAppPath(fileNameStructs.get(0).webAppPath);
//        this.weddingInfoRepository.save(weddingInfo);
//
//        return  new ResponseEntity(HttpStatus.CREATED);
//    }
}
