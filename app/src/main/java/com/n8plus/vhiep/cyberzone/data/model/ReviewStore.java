package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;

public class ReviewStore {
    private String id;
    private String nameCustomer;
    private Store store;
    private int ratingLevel;
    private String review;
    private Date dateReview;

    public ReviewStore(String id, String nameCustomer, Store store, int ratingLevel, String review, Date dateReview) {
        this.id = id;
        this.nameCustomer = nameCustomer;
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

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getRatingLevel() {
        return ratingLevel;
    }

    public void setRatingLevel(int ratingLevel) {
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
