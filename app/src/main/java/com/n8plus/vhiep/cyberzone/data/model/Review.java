package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;

public class Review  {
    private String id;
    private String nameCustomer;
    private String contentReview;
    private int review;
    private Date dateReview;

    public Review(String id, String nameCustomer, String contentReview, int review, Date dateReview) {
        this.id = id;
        this.nameCustomer = nameCustomer;
        this.contentReview = contentReview;
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

    public String getContentReview() {
        return contentReview;
    }

    public void setContentReview(String contentReview) {
        this.contentReview = contentReview;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public Date getDateReview() {
        return dateReview;
    }

    public void setDateReview(Date dateReview) {
        this.dateReview = dateReview;
    }
}
