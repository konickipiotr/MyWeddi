package com.myweddi.modules.gift.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class GeneralGifts {

    @Id
    private Long weddingid;
    private String smallgift;
    private String maingift;
    @Lob
    private String info;

    public GeneralGifts() {
        this.smallgift = "";
        this.maingift = "";
        this.info = "";
    }

    public GeneralGifts(Long weddingid) {
        this();
        this.weddingid = weddingid;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getSmallgift() {
        return smallgift;
    }

    public void setSmallgift(String smallgift) {
        this.smallgift = smallgift;
    }

    public String getMaingift() {
        return maingift;
    }

    public void setMaingift(String maingift) {
        this.maingift = maingift;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
