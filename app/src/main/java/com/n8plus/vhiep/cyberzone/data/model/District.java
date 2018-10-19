package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class District implements Serializable{
    @SerializedName("name_with_type")
    private String name;
    @SerializedName("code")
    private int code;
    @SerializedName("parent_code")
    private int parentCode;

    private List<Ward> wardList;

    public District(String name, int code, int parentCode) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
    }

    public District(String name, int code, int parentCode, List<Ward> wardList) {
        this.name = name;
        this.code = code;
        this.parentCode = parentCode;
        this.wardList = wardList;
    }

    public List<Ward> getWardList() {
        return wardList;
    }

    public void setWardList(List<Ward> wardList) {
        this.wardList = wardList;
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
