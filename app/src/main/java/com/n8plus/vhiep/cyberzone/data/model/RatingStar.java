package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingStar implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("ratingStar")
    private float ratingStar;
    @SerializedName("description")
    private String description;

    public RatingStar(String id, float ratingStar, String description) {
        this.id = id;
        this.ratingStar = ratingStar;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(float ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
