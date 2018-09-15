package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;
import java.util.List;

public class Customer {
    private String id;
    private String name;
    private String gender;
    private Date birthdate;
    private String email;
    private String phone;
    private List<Address> addresses;

    public Customer(String id, String name, String gender, Date birthdate, String email, String phone, List<Address> addresses) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.email = email;
        this.phone = phone;
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}
