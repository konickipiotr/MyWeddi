package com.myweddi.model;

import java.util.List;

public class TableTempObject {

    private List<Integer> capacity;
    private Long weddingid;
    private Long userid;

    public TableTempObject() {
    }

    public TableTempObject(List<Integer> capacity, Long weddingid, Long userid) {
        this.capacity = capacity;
        this.capacity = capacity;
        this.weddingid = weddingid;
        this.userid = userid;
    }

    public List<Integer> getCapacity() {
        return capacity;
    }

    public void setCapacity(List<Integer> capacity) {
        this.capacity = capacity;
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
