package com.myweddi.api;

import com.myweddi.info.WeddingInfo;
import com.myweddi.info.WeddingInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/weddinginfo")
public class WeddingInfoController {

    @Autowired
    private WeddingInfoRepository weddingInfoRepository;

    @PostMapping
    public void addWeddingInfo(@RequestBody WeddingInfo weddingInfo){
        this.weddingInfoRepository.save(weddingInfo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeddingInfo> getChurchInf(@PathVariable("id") Long weddingid){
        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
        if(oWedding.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<WeddingInfo>(oWedding.get(), HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteChurchInfo(@RequestBody Long weddingid){
        this.weddingInfoRepository.deleteById(weddingid);
    }
}
