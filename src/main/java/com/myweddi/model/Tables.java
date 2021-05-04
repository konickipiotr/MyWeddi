package com.myweddi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Tables {

    @Id
    private Long weddingid;
    private int numoftables;
    private int capacity;
    private int free;
    private String realPath;
    private String webAppPath;

    public Tables() {
    }

    public Tables(Long weddingid, int numoftables, List<Integer> capacityVal) {
        this.weddingid = weddingid;
        this.numoftables = numoftables;
        assert numoftables == capacityVal.size();
        for(int i = 0; i < numoftables; i++){
            this.capacity += capacityVal.get(i);
        }
        this.free = this.capacity;
        this.realPath = "";
        this.webAppPath = "";
    }

    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
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

    public int getNumoftables() {
        return numoftables;
    }

    public void setNumoftables(int numoftables) {
        this.numoftables = numoftables;
    }
}
