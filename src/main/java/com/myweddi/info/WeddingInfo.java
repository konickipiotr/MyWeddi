package com.myweddi.info;

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
public class WeddingInfo {
    @Id
    private long weddingid;
    private String name;
    private double longitude;
    private double latitude;
    private String address;
    private String realPath;
    private String webAppPath;

    public WeddingInfo(long weddingid) {
        this.weddingid = weddingid;
    }
}
