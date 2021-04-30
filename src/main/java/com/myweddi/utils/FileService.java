package com.myweddi.utils;

import com.myweddi.conf.Global;
import com.myweddi.exception.FailedSaveFileException;
import org.apache.commons.io.FileUtils;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class FileService {
    private static long photoId = 1;

//    public void createUserDirectory(User user){
//        Path pp = Paths.get(getUserDirPath(user));
//        new File(pp.toAbsolutePath().toString()).mkdirs();
//    }
//
    public void deleteFile(String path) throws IOException {
        new File(path).delete();
    }

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
            wepAppPath = "/" + Global.photosPath + filename;
        }else if(photoCat.equals(PhotoCat.TABLES)){
            realPath = Global.appPath + "tables/";
            wepAppPath = "/tables/" + filename;
        }else if(photoCat.equals(PhotoCat.CHURCH)) {
            realPath = Global.appPath + "church/";
            wepAppPath = "/church/" + filename;
        }else if(photoCat.equals(PhotoCat.PROFILE)){
            realPath = Global.appPath + "profilephoto/";
            wepAppPath = "/profilephoto/" + filename;
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

    public FileNameStruct uploadPhoto(MultipartFile uploadfile, PhotoCat photoCat){
        if (uploadfile == null || uploadfile.isEmpty()) return null;

        FileNameStruct fileNameStruct = createFileName(uploadfile, photoCat);

        Path fullpath = Paths.get(fileNameStruct.realPath);
        try {
            Files.copy(uploadfile.getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileNameStruct;
    }

    public List<FileNameStruct> uploadPhotos(List<MultipartFile> uploadfiles, PhotoCat photoCat){

        if (uploadfiles == null || uploadfiles.isEmpty()) return null;

        List<FileNameStruct> filesNames = new ArrayList<>();
        for(MultipartFile mFile : uploadfiles) {
            filesNames.add(createFileName(mFile, photoCat));
        }

        try {
            for(int i = 0; i < uploadfiles.size(); i++ ) {
                Path fullpath = Paths.get(filesNames.get(i).realPath);
                Files.copy(uploadfiles.get(i).getInputStream(), fullpath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailedSaveFileException();
        }
        return filesNames;

    }

    public List<MultipartFile> convertToMultipartFiles(List<String> imagesStringBytes){
        List<MultipartFile> mFiles = new ArrayList<>();
        for(String sImage : imagesStringBytes){
            byte[] imgbyte = Base64.getDecoder().decode(sImage);
            mFiles.add(new CustomMultipartFile(imgbyte));
        }
        return mFiles;
    }
}
