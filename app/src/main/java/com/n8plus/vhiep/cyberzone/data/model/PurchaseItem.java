package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurchaseItem implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("product")
    private Product product;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("orderItemState")
    private OrderState orderItemState;
    @SerializedName("isReview")
    private boolean isReview;

    public PurchaseItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

//    public PurchaseItem(Product product, Integer quantity, OrderState orderItemState) {
//        this.product = product;
//        this.quantity = quantity;
//        this.orderItemState = orderItemState;
//    }

    public PurchaseItem(String id, String orderId, Product product, Integer quantity, OrderState orderItemState, boolean isReview) {
        this.id = id;
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.orderItemState = orderItemState;
        this.isReview = isReview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public OrderState getOrderItemState() {
        return orderItemState;
    }

    public void setOrderItemState(OrderState orderItemState) {
        this.orderItemState = orderItemState;
    }

    public boolean isReview() {
        return isReview;
    }

    public void setReview(boolean review) {
        isReview = review;
    }
}
