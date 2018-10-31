package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class RegisterSale implements Serializable {
    @SerializedName("_id")
    private String id;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("storeName")
    private String storeName;
    @SerializedName("address")
    private String address;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("registeredDate")
    private Date registeredDate;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("isApprove")
    private Boolean isApprove;

    public RegisterSale(String id, Customer customer, String storeName, String address, String phoneNumber, String email, Date registeredDate, String username, String password) {
        this.id = id;
        this.customer = customer;
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registeredDate = registeredDate;
        this.username = username;
        this.password = password;
    }

    public RegisterSale(String id, Customer customer, String storeName, String address, String phoneNumber, String email, Date registeredDate, String username, String password, Boolean isApprove) {
        this.id = id;
        this.customer = customer;
        this.storeName = storeName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.registeredDate = registeredDate;
        this.username = username;
        this.password = password;
        this.isApprove = isApprove;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getApprove() {
        return isApprove;
    }

    public void setApprove(Boolean approve) {
        isApprove = approve;
    }
}
