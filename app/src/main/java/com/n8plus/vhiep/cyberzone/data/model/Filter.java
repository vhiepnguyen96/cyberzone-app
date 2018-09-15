package com.n8plus.vhiep.cyberzone.data.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class Filter implements ParentObject {

    private String title;
    List<Object> filterItemList;

    public Filter(String title, List<Object> filterItemList) {
        this.title = title;
        this.filterItemList = filterItemList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Object> getFilterItemList() {
        return filterItemList;
    }

    public void setFilterItemList(List<Object> filterItemList) {
        this.filterItemList = filterItemList;
    }

    @Override
    public List<Object> getChildObjectList() {
        return filterItemList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        filterItemList = list;
    }
}
