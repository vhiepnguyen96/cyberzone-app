package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FollowStore implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("store")
    private Store store;

    public FollowStore(String id, Customer customer, Store store) {
        this.id = id;
        this.customer = customer;
        this.store = store;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
