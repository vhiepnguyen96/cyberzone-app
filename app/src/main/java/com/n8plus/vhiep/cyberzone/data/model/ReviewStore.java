package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ReviewStore implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("store")
    private Store store;
    @SerializedName("ratingLevel")
    private RatingLevel ratingLevel;
    @SerializedName("review")
    private String review;
    @SerializedName("dateReview")
    private Date dateReview;

    public ReviewStore(String id, Customer customer, Store store, RatingLevel ratingLevel, String review, Date dateReview) {
        this.id = id;
        this.customer = customer;
        this.store = store;
        this.ratingLevel = ratingLevel;
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

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public RatingLevel getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(RatingLevel ratingLevel) {
        this.ratingLevel = ratingLevel;
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
