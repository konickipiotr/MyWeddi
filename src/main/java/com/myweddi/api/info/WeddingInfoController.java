package com.myweddi.api.info;

import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.WeddingInfo;
import com.myweddi.db.WeddingInfoRepository;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/weddinginfo")
public class WeddingInfoController {

    private WeddingInfoRepository weddingInfoRepository;
    private FileService fileService;

    @Autowired
    public WeddingInfoController(WeddingInfoRepository weddingInfoRepository, FileService fileService) {
        this.weddingInfoRepository = weddingInfoRepository;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<WeddingInfo> addWeddingInfo(@RequestBody WeddingInfo weddingInfo){
        this.weddingInfoRepository.save(weddingInfo);
        return new ResponseEntity<WeddingInfo>(weddingInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingInfo> getChurchInf(@PathVariable("id") Long weddingid){
        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
        if(oWedding.isEmpty())
            return new ResponseEntity<>(HttpStatus.ACCEPTED);

        return new ResponseEntity<WeddingInfo>(oWedding.get(), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteChurchInfo(@RequestBody Long weddingid){
        this.weddingInfoRepository.deleteById(weddingid);
    }

    @PostMapping("/{weddingid}/photo")
    public ResponseEntity saveChurchImg(@PathVariable("weddingid") Long weddingid, MultipartFile[] images){
        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
        if(oWedding.isEmpty()){
            //TODO
        }
        WeddingInfo weddingInfo = oWedding.get();
        FileNameStruct fileNameStructure = fileService.uploadPhotos(images, PhotoCat.WEDDINGHOUSE);
        if(fileNameStructure == null)
            throw new FailedSaveFileException();

        weddingInfo.setRealPath(fileNameStructure.realPath);
        weddingInfo.setWebAppPath(fileNameStructure.webAppPath);
        this.weddingInfoRepository.save(weddingInfo);

        return  new ResponseEntity(HttpStatus.CREATED);
    }
}
