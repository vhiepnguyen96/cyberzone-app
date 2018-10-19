package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FilterChild implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String name;
    @SerializedName("value")
    private String value;
    @SerializedName("isChoose")
    private boolean state;

    public FilterChild(String id, String name, String value, boolean state) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
