package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductType implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("productTypeName")
    private String name;
    @SerializedName("imageURL")
    private String imageURL;

    public ProductType(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductType(String id, String name, String imageURL) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
