package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductPurchase implements Serializable {
    @SerializedName("_id")
    private String productId;
    @SerializedName("count")
    private int count;

    public ProductPurchase(String productId, int count) {
        this.productId = productId;
        this.count = count;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
