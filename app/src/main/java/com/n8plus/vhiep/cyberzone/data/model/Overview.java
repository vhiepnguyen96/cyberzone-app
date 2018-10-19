package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Overview implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("value")
    private String value;

    public Overview(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public Overview(String id, String title, String value) {
        this.id = id;
        this.title = title;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
