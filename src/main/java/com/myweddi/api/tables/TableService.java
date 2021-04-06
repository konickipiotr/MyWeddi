package com.myweddi.api.tables;

import com.myweddi.db.TablePlaceRepository;
import com.myweddi.db.TablesRepository;
import com.myweddi.model.*;
import com.myweddi.user.Guest;
import com.myweddi.user.reposiotry.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class TableService {

    private TablesRepository tablesRepository;
    private TablePlaceRepository tablePlaceRepository;
    private GuestRepository guestRepository;

    @Autowired
    public TableService(TablesRepository tablesRepository, TablePlaceRepository tablePlaceRepository, GuestRepository guestRepository) {
        this.tablesRepository = tablesRepository;
        this.tablePlaceRepository = tablePlaceRepository;
        this.guestRepository = guestRepository;
    }

    public ResponseEntity createTables(TableTempObject tto){

        int[] tablesids = tto.getTablesid();
        int[] capacity = tto.getCapacity();
        Tables tables = new Tables(tto.getWeddingid(), tablesids.length, capacity);

        List<TablePlace> tp = new ArrayList<>();
        for(int i = 0; i < tablesids.length; i++){

            for(int j = 0; j < capacity[i]; j++)
                tp.add(new TablePlace(tablesids[i], (j + 1), tto.getWeddingid()));
        }

        this.tablePlaceRepository.saveAll(tp);
        this.tablesRepository.save(tables);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<TableWrapper> getTableList(Long weddingid){
        Optional<Tables> oTables = this.tablesRepository.findById(weddingid);
        if(oTables.isEmpty()) {
            return new ResponseEntity<TableWrapper>(new TableWrapper(), HttpStatus.OK);
        }

        Tables tables = oTables.get();
        List<TablePlace> tp = new ArrayList<>();
        if(this.tablePlaceRepository.existsByWeddingid(weddingid)){
            tp = this.tablePlaceRepository.findByWeddingid(weddingid);
        }

        TableWrapper tableWrapper = new TableWrapper(tables, tp);
        getAssignedNotAssignedList(tp, weddingid, tableWrapper);
        return new ResponseEntity<TableWrapper>(tableWrapper, HttpStatus.OK);
    }

    private void getAssignedNotAssignedList(List<TablePlace> tp, Long weddingid, TableWrapper tableWrapper){

        List<Guest> guests = this.guestRepository.findByWeddingid(weddingid);
        Map<Long, String> assigned = new HashMap<>();
        Map<Long, String> notAssigned = new HashMap<>();

        for(Guest g : guests){
            boolean notPresent = true;
            for(TablePlace t : tp){
                if(t.getUserid() != null && g.getId().equals(t.getUserid())) {
                    assigned.put(g.getId(), g.getName());
                    t.setUsername(g.getName());
                    notPresent = false;
                }
            }

            if(notPresent)
                notAssigned.put(g.getId(), g.getName());
        }

        tableWrapper.setAssigned(assigned);
        tableWrapper.setNotassigned(notAssigned);
    }
}
