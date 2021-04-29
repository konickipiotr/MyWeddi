package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
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

    public WeddingInfo() {
    }

    public void update(WeddingInfo wi){
        this.name = wi.getName();
        this.longitude = wi.getLongitude();
        this.latitude = wi.getLatitude();
        this.address = wi.getAddress();
    }

    public long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(long weddingid) {
        this.weddingid = weddingid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
