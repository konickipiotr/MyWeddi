package com.myweddi.api.info;

import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.ChurchInfo;
import com.myweddi.db.ChurchRepository;
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
@RequestMapping("/api/churchinfo")
public class ChurchInfoController {

    private ChurchRepository churchRepository;
    private FileService fileService;

    @Autowired
    public ChurchInfoController(ChurchRepository churchRepository, FileService fileService) {
        this.churchRepository = churchRepository;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<ChurchInfo>  addChurchInfo(@RequestBody ChurchInfo churchInfo){
        this.churchRepository.save(churchInfo);
        return new ResponseEntity<ChurchInfo>(churchInfo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChurchInfo> getChurchInf(@PathVariable("id") Long weddingid){
        Optional<ChurchInfo> oChurch = this.churchRepository.findById(weddingid);
        if(oChurch.isEmpty())
            return new ResponseEntity<ChurchInfo>(new ChurchInfo(), HttpStatus.ACCEPTED);

        return new ResponseEntity<ChurchInfo>(oChurch.get(), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteChurchInfo(@RequestBody Long weddingid){
        this.churchRepository.deleteById(weddingid);
    }

    @PostMapping("/photo/{weddingid}")
    public ResponseEntity saveChurchImg(@PathVariable("weddingid") Long weddingid, MultipartFile[] images){
        Optional<ChurchInfo> oChurch = this.churchRepository.findById(weddingid);
        if(oChurch.isEmpty()){
            //TODO
        }
        ChurchInfo churchInfo = oChurch.get();
        List<MultipartFile> mList = new ArrayList<>(Arrays.asList(images));
        List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(mList, PhotoCat.WEDDINGHOUSE);
        if(fileNameStructs == null || fileNameStructs.isEmpty())
            throw new FailedSaveFileException();

        churchInfo.setRealPath(fileNameStructs.get(0).realPath);
        churchInfo.setWebAppPath(fileNameStructs.get(0).webAppPath);
        this.churchRepository.save(churchInfo);

        return  new ResponseEntity(HttpStatus.CREATED);
    }

}
