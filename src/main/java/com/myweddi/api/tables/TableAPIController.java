package com.myweddi.api.tables;

import com.myweddi.model.TableTempObject;
import com.myweddi.model.TableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/table")
public class TableAPIController {

    @Autowired
    private TableService tableService;

    @GetMapping("/{weddingid}")
    public ResponseEntity<TableWrapper> getTables(@PathVariable("weddingid") Long weddingid){
        return tableService.getTableList(weddingid);

    }

    @PostMapping
    public ResponseEntity createTables(@RequestBody TableTempObject tableObject){
        return tableService.createTables(tableObject);
    }

    @PostMapping("/setguests")
    public ResponseEntity setGuests(@RequestBody String vVal[], Principal principal){
        tableService.updateTables(vVal, principal);
        return  new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/loadschema")
    public ResponseEntity loadTableSchema(@RequestBody String stringByteImage, Principal principal){
        tableService.saveTableSchema(stringByteImage, principal);
        return new ResponseEntity(HttpStatus.OK);
    }
}
