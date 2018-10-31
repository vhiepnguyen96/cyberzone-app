package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RatingLevel implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("ratingLevel")
    private int level;
    @SerializedName("description")
    private String description;

    public RatingLevel(String id, int level, String description) {
        this.id = id;
        this.level = level;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
