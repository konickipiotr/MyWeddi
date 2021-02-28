package com.myweddi.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

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
