package com.n8plus.vhiep.cyberzone.data.model;

import java.util.Date;

public class HistoryReview {
    private ReviewStore reviewStore;
    private ReviewProduct reviewProduct;

    public HistoryReview(ReviewStore reviewStore, ReviewProduct reviewProduct) {
        this.reviewStore = reviewStore;
        this.reviewProduct = reviewProduct;
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
