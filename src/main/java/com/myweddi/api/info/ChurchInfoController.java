package com.myweddi.api.info;

import com.myweddi.info.ChurchInfo;
import com.myweddi.info.ChurchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/churchinfo")
public class ChurchInfoController {

    @Autowired
    private ChurchRepository churchRepository;

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
}
