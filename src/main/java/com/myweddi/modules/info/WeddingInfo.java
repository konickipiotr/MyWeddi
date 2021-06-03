package com.myweddi.modules.info;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Entity
public class WeddingInfo {

    @Id
    private Long weddingid;
    private String churchname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ceremenytime;
    @Transient
    private String sceremenytime;
    private double chLongitude;
    private double chLatitude;
    private String churchaddress;
    private String info;
    private String chRealPath;
    private String chWebAppPath;

    private String weddinghousename;
    private double wLongitude;
    private double wLatitude;
    private String wAddress;
    private String wRealPath;
    private String wWebAppPath;
    private String weddingcode;

    public void update(WeddingInfo weddingInfo) {
        this.churchname = weddingInfo.getChurchname();
        this.ceremenytime = weddingInfo.getCeremenytime();
        this.chLongitude = weddingInfo.getChLongitude();
        this.chLatitude = weddingInfo.getChLatitude();
        this.churchaddress = weddingInfo.getChurchaddress();
        this.info = weddingInfo.getInfo();

        this.weddinghousename = weddingInfo.getWeddinghousename();
        this.wLongitude = weddingInfo.getwLongitude();
        this.wLatitude = weddingInfo.getwLatitude();
        this.wAddress = weddingInfo.getwAddress();
    }

    public WeddingInfo(Long weddingid) {
        this.weddingid = weddingid;
    }

    public WeddingInfo() {
    }

    public void convertDate(){
        if(this.ceremenytime == null){
            this.sceremenytime = "";
            return;
        }
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        this.sceremenytime = sf.format(Date.valueOf(this.ceremenytime.toLocalDate()));
        int minute = this.ceremenytime.getMinute();
        this.sceremenytime += " " + this.ceremenytime.getHour() + ":" + (minute > 9 ? minute : "0" + minute) ;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getChurchname() {
        return churchname;
    }

    public void setChurchname(String churchname) {
        this.churchname = churchname;
    }

    public LocalDateTime getCeremenytime() {
        return ceremenytime;
    }

    public void setCeremenytime(LocalDateTime ceremenytime) {
        this.ceremenytime = ceremenytime;
    }

    public double getChLongitude() {
        return chLongitude;
    }

    public void setChLongitude(double chLongitude) {
        this.chLongitude = chLongitude;
    }

    public double getChLatitude() {
        return chLatitude;
    }

    public void setChLatitude(double chLatitude) {
        this.chLatitude = chLatitude;
    }

    public String getChurchaddress() {
        return churchaddress;
    }

    public void setChurchaddress(String churchaddress) {
        this.churchaddress = churchaddress;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getChRealPath() {
        return chRealPath;
    }

    public void setChRealPath(String chRealPath) {
        this.chRealPath = chRealPath;
    }

    public String getChWebAppPath() {
        return chWebAppPath;
    }

    public void setChWebAppPath(String chWebAppPath) {
        this.chWebAppPath = chWebAppPath;
    }

    public String getWeddinghousename() {
        return weddinghousename;
    }

    public void setWeddinghousename(String weddinghousename) {
        this.weddinghousename = weddinghousename;
    }

    public double getwLongitude() {
        return wLongitude;
    }

    public void setwLongitude(double wLongitude) {
        this.wLongitude = wLongitude;
    }

    public double getwLatitude() {
        return wLatitude;
    }

    public void setwLatitude(double wLatitude) {
        this.wLatitude = wLatitude;
    }

    public String getwAddress() {
        return wAddress;
    }

    public void setwAddress(String wAddress) {
        this.wAddress = wAddress;
    }

    public String getwRealPath() {
        return wRealPath;
    }

    public void setwRealPath(String wRealPath) {
        this.wRealPath = wRealPath;
    }

    public String getwWebAppPath() {
        return wWebAppPath;
    }

    public void setwWebAppPath(String wWebAppPath) {
        this.wWebAppPath = wWebAppPath;
    }

    public String getWeddingcode() {
        return weddingcode;
    }

    public void setWeddingcode(String weddingcode) {
        this.weddingcode = weddingcode;
    }

    public String getSceremenytime() {
        return sceremenytime;
    }

    public void setSceremenytime(String sceremenytime) {
        this.sceremenytime = sceremenytime;
    }
}
