package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private List<PurchaseItem> purchaseList;
    private int totalQuantity;
    private Address deliveryAddress;
    private String transportFee;
    private String totalPrice;
    private Date purchaseDate;
    private String orderState;

    public Order(String orderId, String customerId, List<PurchaseItem> purchaseList, int totalQuantity, Address deliveryAddress, String transportFee, String totalPrice, Date purchaseDate, String orderState) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.purchaseList = purchaseList;
        this.totalQuantity = totalQuantity;
        this.deliveryAddress = deliveryAddress;
        this.transportFee = transportFee;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.orderState = orderState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getTransportFee() {
        return transportFee;
    }

    public void setTransportFee(String transportFee) {
        this.transportFee = transportFee;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }
}
