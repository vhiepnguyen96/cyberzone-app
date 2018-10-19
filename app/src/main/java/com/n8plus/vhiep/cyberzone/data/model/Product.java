package com.n8plus.vhiep.cyberzone.data.model;

import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    @SerializedName("_id")
    private String productId;
    @SerializedName("productType")
    private ProductType productType;
    @SerializedName("store")
    private Store store;
    @SerializedName("productName")
    private String productName;
    @SerializedName("imageList")
    private List<ProductImage> imageList;
    @SerializedName("price")
    private String price;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("specifications")
    private List<Specification> specifications;
    @SerializedName("overviews")
    private List<Overview> overviews;
    @SerializedName("state")
    private String state;
    @SerializedName("saleOff")
    private SaleOff saleOff;
    @SerializedName("reviewProducts")
    private List<ReviewProduct> reviewProducts;

    public Product(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    public Product(String productId, String productName, List<ProductImage> imageList) {
        this.productId = productId;
        this.productName = productName;
        this.imageList = imageList;
    }

    public Product(String productId, ProductType productType, Store store, String productName, List<ProductImage> imageList, String price, String state, SaleOff saleOff) {
        this.productId = productId;
        this.productType = productType;
        this.store = store;
        this.productName = productName;
        this.imageList = imageList;
        this.price = price;
        this.state = state;
        this.saleOff = saleOff;
    }

    public Product(String productId, ProductType productType, Store store, String productName, List<ProductImage> imageList, String price, int quantity, List<Specification> specifications, List<Overview> overviews, String state, SaleOff saleOff, List<ReviewProduct> reviewProducts) {
        this.productId = productId;
        this.productType = productType;
        this.store = store;
        this.productName = productName;
        this.imageList = imageList;
        this.price = price;
        this.quantity = quantity;
        this.specifications = specifications;
        this.overviews = overviews;
        this.state = state;
        this.saleOff = saleOff;
        this.reviewProducts = reviewProducts;
    }

    public Product(String productId, ProductType productType, Store store, String productName, String price, List<Specification> specifications, List<Overview> overviews, String state, SaleOff saleOff) {
        this.productId = productId;
        this.productType = productType;
        this.store = store;
        this.productName = productName;
        this.price = price;
        this.specifications = specifications;
        this.overviews = overviews;
        this.state = state;
        this.saleOff = saleOff;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<ProductImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<ProductImage> imageList) {
        this.imageList = imageList;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<Specification> specifications) {
        this.specifications = specifications;
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

    public SaleOff getSaleOff() {
        return saleOff;
    }

    public void setSaleOff(SaleOff saleOff) {
        this.saleOff = saleOff;
    }

    public List<ReviewProduct> getReviewProducts() {
        return reviewProducts;
    }

    public void setReviewProducts(List<ReviewProduct> reviewProducts) {
        this.reviewProducts = reviewProducts;
    }

    public static float convertToPrice(String price) {
        return (float) Math.round((Float.valueOf(price) / 1000) * 1000) / 1000;
    }
}
