package com.n8plus.vhiep.cyberzone.data.model;

import java.util.List;

public class Bill {
    private String id;
    private String customerId;
    private List<PurchaseItem> purchaseList;
    private Float TransportFee;
    private Float TotalPrice;
    private String State;

    public Bill(String id, String customerId, List<PurchaseItem> purchaseList, Float transportFee, Float totalPrice, String state) {
        this.id = id;
        this.customerId = customerId;
        this.purchaseList = purchaseList;
        TransportFee = transportFee;
        TotalPrice = totalPrice;
        State = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Float getTransportFee() {
        return TransportFee;
    }

    public void setTransportFee(Float transportFee) {
        TransportFee = transportFee;
    }

    public Float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
