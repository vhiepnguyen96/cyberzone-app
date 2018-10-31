package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;

public class HistoryReview {
    private Date dateReview;
    private ReviewStore reviewStore;
    private ReviewProduct reviewProduct;

    public HistoryReview(ReviewStore reviewStore) {
        this.reviewStore = reviewStore;
    }

    public HistoryReview(ReviewProduct reviewProduct) {
        this.reviewProduct = reviewProduct;
    }

    public HistoryReview(ReviewStore reviewStore, ReviewProduct reviewProduct) {
        this.reviewStore = reviewStore;
        this.reviewProduct = reviewProduct;
    }

    public HistoryReview(Date dateReview, ReviewStore reviewStore, ReviewProduct reviewProduct) {
        this.dateReview = dateReview;
        this.reviewStore = reviewStore;
        this.reviewProduct = reviewProduct;
    }

    public Date getDateReview() {
        return dateReview;
    }

    public void setDateReview(Date dateReview) {
        this.dateReview = dateReview;
    }

    public ReviewStore getReviewStore() {
        return reviewStore;
    }

    public void setReviewStore(ReviewStore reviewStore) {
        this.reviewStore = reviewStore;
    }

    public ReviewProduct getReviewProduct() {
        return reviewProduct;
    }

    public void setReviewProduct(ReviewProduct reviewProduct) {
        this.reviewProduct = reviewProduct;
    }
}
