package com.n8plus.vhiep.cyberzone.data.model;

import java.util.List;

public class WishList {
    private String customerId;
    private List<Product> productList;

    public WishList(String customerId, List<Product> productList) {
        this.customerId = customerId;
        this.productList = productList;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
