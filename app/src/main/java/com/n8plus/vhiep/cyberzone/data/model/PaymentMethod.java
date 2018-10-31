package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentMethod implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("paymentMethodName")
    private String name;
    @SerializedName("description")
    private String description;

    public PaymentMethod(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public PaymentMethod(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
