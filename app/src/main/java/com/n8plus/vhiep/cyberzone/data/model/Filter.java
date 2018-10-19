package com.n8plus.vhiep.cyberzone.data.model;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Filter implements ParentObject {
    @SerializedName("_id")
    private String id;
    @SerializedName("filterName")
    private String filterName;
    @SerializedName("filterItems")
    private List<FilterChild> filterItems;

    public Filter(String id, String filterName, List<FilterChild> filterItems) {
        this.id = id;
        this.filterName = filterName;
        this.filterItems = filterItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<FilterChild> getFilterItems() {
        return filterItems;
    }

    public void setFilterItems(List<FilterChild> filterItems) {
        this.filterItems = filterItems;
    }

//    @Override
//    public List<Object> getChildObjectList() {
//        return filterItems;
//    }
//
//    @Override
//    public void setChildObjectList(List<Object> list) {
//        filterItems = list;
//    }

    @Override
    public List<Object> getChildObjectList() {
        return new ArrayList<Object>(filterItems);
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        filterItems = (List<FilterChild>) (Object) list;
    }
}
