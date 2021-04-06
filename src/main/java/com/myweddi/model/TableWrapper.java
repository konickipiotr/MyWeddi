package com.myweddi.model;

import java.util.List;
import java.util.Map;

public class TableWrapper {

    private Tables tables;
    private List<TablePlace> tablePlaces;
    private Map<Long, String> assigned;
    private Map<Long, String> notassigned;


    public TableWrapper() {
    }

    public TableWrapper(Tables tables, List<TablePlace> tablePlaces) {
        this.tables = tables;
        this.tablePlaces = tablePlaces;
    }

    public Tables getTables() {
        return tables;
    }

    public void setTables(Tables tables) {
        this.tables = tables;
    }

    public List<TablePlace> getTablePlaces() {
        return tablePlaces;
    }

    public void setTablePlaces(List<TablePlace> tablePlaces) {
        this.tablePlaces = tablePlaces;
    }

    public Map<Long, String> getAssigned() {
        return assigned;
    }

    public void setAssigned(Map<Long, String> assigned) {
        this.assigned = assigned;
    }

    public Map<Long, String> getNotassigned() {
        return notassigned;
    }

    public void setNotassigned(Map<Long, String> notassigned) {
        this.notassigned = notassigned;
    }
}
