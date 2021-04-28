package com.myweddi.webapp.host.info;

import com.myweddi.model.ChurchInfo;
import com.myweddi.model.WeddingInfo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.LocalDateTime;

public class WeddingDTO {

    private Long weddingid;
    private String churchname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ceremenytime;
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

    public void setChurchInfo(ChurchInfo ch){
        this.churchname = ch.getName();
        this.ceremenytime = ch.getCeremenytime();
        this.chLatitude = ch.getLatitude();
        this.chLongitude = ch.getLongitude();
        this.info = ch.getInfo();
        this.churchaddress = ch.getAddress();
        this.chWebAppPath = ch.getWebAppPath();
    }

    public ChurchInfo getChurchInfo(){
        ChurchInfo churchInfo = new ChurchInfo();
        churchInfo.setWeddingid(this.weddingid);
        churchInfo.setCeremenytime(this.ceremenytime);
        churchInfo.setName(churchname);
        churchInfo.setAddress(churchaddress);
        churchInfo.setLongitude(chLongitude);
        churchInfo.setLatitude(chLatitude);
        churchInfo.setInfo(info);
        churchInfo.setRealPath(chRealPath);
        churchInfo.setWebAppPath(chWebAppPath);
        return churchInfo;
    }

    public void setWeddingInfo(WeddingInfo wf){
        this.weddinghousename = wf.getName();
        this.wAddress = wf.getAddress();
        this.wLatitude = wf.getLatitude();
        this.wLongitude = wf.getLongitude();
        this.wRealPath = wf.getRealPath();
        this.wWebAppPath = wf.getWebAppPath();
    }

    public WeddingInfo getWeddingInfo(){
        WeddingInfo wi = new WeddingInfo();
        wi.setWeddingid(weddingid);
        wi.setName(weddinghousename);
        wi.setAddress(wAddress);
        wi.setLatitude(wLatitude);
        wi.setLongitude(wLongitude);
        wi.setRealPath(wRealPath);
        wi.setWebAppPath(wWebAppPath);
        return wi;
    }

    public WeddingDTO(Long weddingid) {
        this.weddingid = weddingid;
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
}
