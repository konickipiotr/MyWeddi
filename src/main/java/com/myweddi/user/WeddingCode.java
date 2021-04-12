package com.myweddi.user;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WeddingCode {

    @Id
    private Long weddingid;
    private String weddingcode;

    public WeddingCode() {
    }

    public WeddingCode(Long weddingid, String weddingcode) {
        this.weddingid = weddingid;
        this.weddingcode = weddingcode;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getWeddingcode() {
        return weddingcode;
    }

    public void setWeddingcode(String weddingcode) {
        this.weddingcode = weddingcode;
    }
}
