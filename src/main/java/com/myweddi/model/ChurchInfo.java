package com.myweddi.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class ChurchInfo {

    @Id
    private Long weddingid;
    private String name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ceremenytime;
    private double longitude;
    private double latitude;
    private String address;
    @Lob
    private String info;
    private String realPath;
    private String webAppPath;

    public ChurchInfo(Long weddingid) {
        this.weddingid = weddingid;
    }
}
