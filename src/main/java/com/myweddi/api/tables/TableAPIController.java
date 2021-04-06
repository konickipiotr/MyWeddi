package com.myweddi.api.tables;

import com.myweddi.model.TableTempObject;
import com.myweddi.model.TableWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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
}
