package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Entity
public class ChurchInfo {

    @Id
    private Long weddingid;
    private String name;
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

    public ChurchInfo() {
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCeremenytime() {
        return ceremenytime;
    }

    public void setCeremenytime(LocalDateTime ceremenytime) {
        this.ceremenytime = ceremenytime;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }
}
