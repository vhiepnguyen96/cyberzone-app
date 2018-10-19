package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Store implements Serializable{
    @SerializedName("_id")
    private String storeId;
    @SerializedName("account")
    private Account account;
    @SerializedName("storeName")
    private String storeName;
    @SerializedName("location")
    private String location;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("createdDate")
    private Date createdDate;
    @SerializedName("categories")
    private List<Category> categories;

    public Store(String storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public Store(String storeId, String storeName, String location) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
    }

    public Store(String storeId, Account account, String storeName, String location, String phoneNumber, Date createdDate, List<Category> categories) {
        this.storeId = storeId;
        this.account = account;
        this.storeName = storeName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.categories = categories;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
