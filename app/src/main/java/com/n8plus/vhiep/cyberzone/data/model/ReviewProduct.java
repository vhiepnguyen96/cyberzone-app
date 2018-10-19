package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ReviewProduct implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("product")
    private Product product;
    @SerializedName("ratingStar")
    private RatingStar ratingStar;
    @SerializedName("review")
    private String review;
    @SerializedName("dateReview")
    private Date dateReview;

    public ReviewProduct(String id, Customer customer, Product product, RatingStar ratingStar, String review, Date dateReview) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.ratingStar = ratingStar;
        this.review = review;
        this.dateReview = dateReview;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public RatingStar getRatingStar() {
        return ratingStar;
    }

    public void setRatingStar(RatingStar ratingStar) {
        this.ratingStar = ratingStar;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Date getDateReview() {
        return dateReview;
    }

    public void setDateReview(Date dateReview) {
        this.dateReview = dateReview;
    }
}
