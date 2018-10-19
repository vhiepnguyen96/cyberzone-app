package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryPrice implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("productQuantity")
    private int productQuantity;
    @SerializedName("transportFee")
    private String transportFee;
    @SerializedName("description")
    private String description;

    public DeliveryPrice(String id, int productQuantity, String transportFee, String description) {
        this.id = id;
        this.productQuantity = productQuantity;
        this.transportFee = transportFee;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(String transportFee) {
        this.transportFee = transportFee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
