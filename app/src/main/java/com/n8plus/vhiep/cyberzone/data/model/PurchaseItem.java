package com.n8plus.vhiep.cyberzone.data.model;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    private Product product;
    private Integer quantity;

    public PurchaseItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
