package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderState implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("orderStateName")
    private String stateName;
    @SerializedName("description")
    private String description;

    public OrderState(String id, String stateName, String description) {
        this.id = id;
        this.stateName = stateName;
        this.description = description;
    }

    public OrderState(String id, String stateName) {
        this.id = id;
        this.stateName = stateName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
