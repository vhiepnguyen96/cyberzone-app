package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductPurchase implements Serializable {
    @SerializedName("_id")
    private Product product;
    @SerializedName("count")
    private int count;

    public ProductPurchase(Product product, int count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
