package com.myweddi.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class ListWrapper<T> {

    private List<T> list;

    public ListWrapper() {
    }

    public ListWrapper(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
