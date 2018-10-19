package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ward implements Serializable{
    @SerializedName("name_with_type")
    private String name;
    @SerializedName("code")
    private int code;
    @SerializedName("parent_code")
    private int parentCode;

    public Ward(String name, int code, int parentCode) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }
}
