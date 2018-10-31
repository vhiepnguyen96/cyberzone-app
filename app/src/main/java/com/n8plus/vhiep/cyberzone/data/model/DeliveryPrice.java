package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryPrice implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("totalPriceMin")
    private int totalPriceMin;
    @SerializedName("totalPriceMax")
    private int totalPriceMax;
    @SerializedName("transportFee")
    private String transportFee;
    @SerializedName("description")
    private String description;

    public DeliveryPrice(String id, int totalPriceMin, int totalPriceMax, String transportFee, String description) {
        this.id = id;
        this.totalPriceMin = totalPriceMin;
        this.totalPriceMax = totalPriceMax;
        this.transportFee = transportFee;
        this.description = description;
    }

    public DeliveryPrice(String id, String transportFee) {
        this.id = id;
        this.transportFee = transportFee;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalPriceMin() {
        return totalPriceMin;
    }

    public void setTotalPriceMin(int totalPriceMin) {
        this.totalPriceMin = totalPriceMin;
    }

    public int getTotalPriceMax() {
        return totalPriceMax;
    }

    public void setTotalPriceMax(int totalPriceMax) {
        this.totalPriceMax = totalPriceMax;
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
