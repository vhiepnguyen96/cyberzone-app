package com.n8plus.vhiep.cyberzone.data.model;

public class Address {
    private String id;
    private String nameCustomer;
    private String phone;
    private String address;
    private boolean isDefault;

    public Address(String id, String nameCustomer, String phone, String address, boolean isDefault) {
        this.id = id;
        this.nameCustomer = nameCustomer;
        this.phone = phone;
        this.address = address;
        this.isDefault = isDefault;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
