package com.myweddi.api.tables;

import com.myweddi.db.PhotoRepository;
import com.myweddi.db.TablePlaceRepository;
import com.myweddi.db.TablesRepository;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.*;
import com.myweddi.user.Guest;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.PhotoCat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;

@Service
@Transactional
public class TableService {

    private TablesRepository tablesRepository;
    private TablePlaceRepository tablePlaceRepository;
    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private FileService fileService;
    private PhotoRepository photoRepository;

    @Autowired
    public TableService(TablesRepository tablesRepository, TablePlaceRepository tablePlaceRepository, GuestRepository guestRepository, UserAuthRepository userAuthRepository, FileService fileService, PhotoRepository photoRepository) {
        this.tablesRepository = tablesRepository;
        this.tablePlaceRepository = tablePlaceRepository;
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.fileService = fileService;
        this.photoRepository = photoRepository;
    }

    public ResponseEntity createTables(TableTempObject tto){

        List<Integer> capacity = tto.getCapacity();
        Tables tables = new Tables(tto.getWeddingid(), capacity.size(), capacity);

        if(tablePlaceRepository.existsByWeddingid(tto.getWeddingid()))
            tablePlaceRepository.deleteByWeddingid(tto.getWeddingid());

        List<TablePlace> tp = new ArrayList<>();
        for(int i = 0; i < capacity.size(); i++){

            for(int j = 0; j < capacity.get(i); j++)
                tp.add(new TablePlace(i, j, tto.getWeddingid()));
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

    public void updateTables(String vVal[], Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Tables tables = tablesRepository.findById(user.getId()).get();
        List<TablePlace> tablePlaces = tablePlaceRepository.findByWeddingid(user.getId());

        int occupy = 0;
        for(TablePlace t : tablePlaces){
            for(String v : vVal){
                String sArr[] = v.split("_");
                int tableid = Integer.valueOf(sArr[0]);
                int place = Integer.valueOf(sArr[1]);
                long userid = Long.valueOf(sArr[2]);

                if(tableid == t.getTableid() && place == t.getPlaceid()) {
                    t.setUserid(userid);
                    if(userid != -1)
                        occupy++;
                    break;
                }
            }
        }

        tables.setFree(tables.getCapacity() - occupy);
        tablesRepository.save(tables);
        tablePlaceRepository.saveAll(tablePlaces);
    }

    public void saveTableSchema(String stringByteImage, Principal principal){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Tables tables = tablesRepository.findById(user.getId()).get();

        List<String> stringBytes = new ArrayList<>(Arrays.asList(stringByteImage));
        List<MultipartFile> multipartFiles = fileService.convertToMultipartFiles(stringBytes);
        List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(multipartFiles, PhotoCat.TABLES);
        if (fileNameStructs == null || fileNameStructs.isEmpty())
            throw new FailedSaveFileException();

        tables.setRealPath(fileNameStructs.get(0).realPath);
        tables.setWebAppPath(fileNameStructs.get(0).webAppPath);
        this.tablesRepository.save(tables);
    }
}
