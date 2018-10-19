package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Address implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("presentation")
    private String presentation;
    @SerializedName("phoneNumber")
    private String phone;
    @SerializedName("address")
    private String address;

    public Address(String id, String presentation, String phone, String address) {
        this.id = id;
        this.presentation = presentation;
        this.phone = phone;
        this.address = address;
    }

    public Address(String presentation, String phone, String address) {
        this.presentation = presentation;
        this.phone = phone;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
