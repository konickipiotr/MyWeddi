package com.myweddi.utils;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class FileService {
    private static long photoId = 1;

//    public void createUserDirectory(User user){
//        Path pp = Paths.get(getUserDirPath(user));
//        new File(pp.toAbsolutePath().toString()).mkdirs();
//    }
//
//    public void deleteUserDirectory(User user) throws IOException {
//        Path pp = Paths.get(getUserDirPath(user));
//        FileUtils.deleteDirectory(new File(pp.toAbsolutePath().toString()));
//    }

    private FileNameStruct createFileName(MultipartFile mFile, PhotoCat photoCat){
        String orginalFileName = mFile.getOriginalFilename();
        String fileType = orginalFileName.substring(orginalFileName.length() - 3);

        LocalDateTime now = LocalDateTime.now(Global.zid);
        now = now.truncatedTo(ChronoUnit.SECONDS);

        String filename = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + photoId++ + "." + fileType;

        String realPath = "";
        String wepAppPath = "";
        if(photoCat.equals(PhotoCat.POST)) {
            realPath = Global.appPath + Global.photosPath;
            wepAppPath = Global.photosPath + filename;
        }else if(photoCat.equals(PhotoCat.CHURCH)){
            realPath = Global.appPath + "church/";
            wepAppPath = "/church/" + filename;
        }else {
            realPath = Global.appPath + "weddinghouse/";
            wepAppPath = "/weddinghouse/" + filename;
        }

        File dir = new File(realPath);
        if(!dir.exists())
            dir.mkdirs();

        realPath += filename;


        return new FileNameStruct(realPath, wepAppPath);
    }

    public FileNameStruct uploadPhotos(MultipartFile[] uploadfiles, PhotoCat photoCat){
        if (uploadfiles.length < 1) return null;
        if (uploadfiles[0].isEmpty()) return null;

        FileNameStruct fileName = createFileName(uploadfiles[0], photoCat);

        try {
            Path fullpath = Paths.get(fileName.realPath);
            Files.copy(uploadfiles[0].getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailedSaveFileException();
        }
        return fileName;

    }

}
