package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WishList implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("product")
    private Product product;

    public WishList(String id, Customer customer, Product product) {
        this.id = id;
        this.customer = customer;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

