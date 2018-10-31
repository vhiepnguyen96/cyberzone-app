package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    @SerializedName("_id")
    private String orderId;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("deliveryAddress")
    private Address deliveryAddress;
    @SerializedName("deliveryPrice")
    private DeliveryPrice deliveryPrice;
    @SerializedName("totalQuantity")
    private int totalQuantity;
    @SerializedName("totalPrice")
    private String totalPrice;
    @SerializedName("purchaseDate")
    private Date purchaseDate;
    @SerializedName("paymentMethod")
    private PaymentMethod paymentMethod;
    @SerializedName("orderState")
    private OrderState orderState;
    @SerializedName("orderItems")
    private List<PurchaseItem> purchaseList;

    public Order(String orderId, Customer customer, Address deliveryAddress, DeliveryPrice deliveryPrice, int totalQuantity, String totalPrice, Date purchaseDate, PaymentMethod paymentMethod, OrderState orderState) {
        this.orderId = orderId;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPrice = deliveryPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.orderState = orderState;
    }

    public Order(String orderId, Customer customer, Address deliveryAddress, DeliveryPrice deliveryPrice, int totalQuantity, String totalPrice, Date purchaseDate, PaymentMethod paymentMethod, OrderState orderState, List<PurchaseItem> purchaseList) {
        this.orderId = orderId;
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPrice = deliveryPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.orderState = orderState;
        this.purchaseList = purchaseList;
    }

    public Order(Customer customer, Address deliveryAddress, DeliveryPrice deliveryPrice, int totalQuantity, String totalPrice, List<PurchaseItem> purchaseList) {
        this.customer = customer;
        this.deliveryAddress = deliveryAddress;
        this.deliveryPrice = deliveryPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.purchaseList = purchaseList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public DeliveryPrice getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(DeliveryPrice deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public List<PurchaseItem> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<PurchaseItem> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public String getPurchaseDateCustom() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(purchaseDate);
        String date = calendar.get(Calendar.DATE) + " Thg " + (calendar.get(Calendar.MONTH) + 1) + " " + calendar.get(Calendar.YEAR);
        return date;
    }
}
