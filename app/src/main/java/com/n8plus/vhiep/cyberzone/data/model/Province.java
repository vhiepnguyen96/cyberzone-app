package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Province implements Serializable{
    @SerializedName("name_with_type")
    private String name;
    @SerializedName("code")
    private int code;
    private List<District> districtList;

    public Province(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public Province(String name, int code, List<District> districtList) {
        this.name = name;
        this.code = code;
        this.districtList = districtList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
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
}
