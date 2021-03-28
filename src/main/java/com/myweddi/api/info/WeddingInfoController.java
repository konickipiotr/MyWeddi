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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public ResponseEntity saveChurchImg(@PathVariable("weddingid") Long weddingid, MultipartFile[] images){ //TODO byteAsString
        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
        if(oWedding.isEmpty()){
            //TODO
        }
        WeddingInfo weddingInfo = oWedding.get();

        List<MultipartFile> mList = new ArrayList<>(Arrays.asList(images));

        List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(mList, PhotoCat.WEDDINGHOUSE);
        if(fileNameStructs == null || fileNameStructs.isEmpty())
            throw new FailedSaveFileException();

        weddingInfo.setRealPath(fileNameStructs.get(0).realPath);
        weddingInfo.setWebAppPath(fileNameStructs.get(0).webAppPath);
        this.weddingInfoRepository.save(weddingInfo);

        return  new ResponseEntity(HttpStatus.CREATED);
    }
}
