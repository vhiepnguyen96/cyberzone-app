package com.n8plus.vhiep.cyberzone.data.model;

import java.util.List;

public class Product {
    private String id;
    private String name;
    private int image;
    private List<Specification> specifications;
    private String price;
    private List<Overview> overviews;
    private String state;
    private int discount;

    public Product(String id, String name, int image, List<Specification> specifications, String price, List<Overview> overviews, String state, int discount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.specifications = specifications;
        this.price = price;
        this.overviews = overviews;
        this.state = state;
        this.discount = discount;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<Overview> getOverviews() {
        return overviews;
    }

    public void setOverviews(List<Overview> overviews) {
        this.overviews = overviews;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
