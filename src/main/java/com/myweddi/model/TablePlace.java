package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TablePlace {

    @Id
    @GeneratedValue
    private Long id;
    private Long userid;
    private int tableid;
    private int placeid;
    private Long weddingid;

    @Transient
    private String username;

    public TablePlace() {
    }

    public TablePlace(int tableid, int placeid, Long weddingid) {
        this.userid = -1l;
        this.tableid = tableid;
        this.placeid = placeid;
        this.weddingid = weddingid;
    }

    public TablePlace(Long userid, int tableid, int placeid, Long weddingid) {
        this.userid = userid;
        this.tableid = tableid;
        this.placeid = placeid;
        this.weddingid = weddingid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public int getTableid() {
        return tableid;
    }

    public void setTableid(int tableid) {
        this.tableid = tableid;
    }

    public int getPlaceid() {
        return placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
