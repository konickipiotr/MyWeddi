package com.myweddi.info;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Data
public class ChurchInfo {

    @Id
    private Long weddingid;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ceremenytime;
    private double longitude;
    private double latitude;
    @Lob
    private String info;
    private String realPath;
    private String webAppPath;
}