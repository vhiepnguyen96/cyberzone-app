package com.n8plus.vhiep.cyberzone.data.model;

public class Store {
    private String storeId;
    private String storeName;
    private String representative;
    private String location;

    public Store(String storeId, String storeName, String representative, String location) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.representative = representative;
        this.location = location;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
