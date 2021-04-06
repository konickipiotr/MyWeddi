package com.myweddi.model;

public class TableTempObject {

    private int tablesid[];
    private int capacity[];
    private Long weddingid;
    private Long userid;

    public TableTempObject() {
    }

    public TableTempObject(int[] tablesid, int[] capacity, Long weddingid, Long userid) {
        this.tablesid = tablesid;
        this.capacity = capacity;
        this.weddingid = weddingid;
        this.userid = userid;
    }

    public int[] getTablesid() {
        return tablesid;
    }

    public void setTablesid(int[] tablesid) {
        this.tablesid = tablesid;
    }

    public int[] getCapacity() {
        return capacity;
    }

    public void setCapacity(int[] capacity) {
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
