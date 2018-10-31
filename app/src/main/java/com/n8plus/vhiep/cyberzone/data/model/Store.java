package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Store implements Serializable {
    @SerializedName("_id")
    private String storeId;
    @SerializedName("account")
    private Account account;
    @SerializedName("storeName")
    private String storeName;
    @SerializedName("location")
    private String location;
    @SerializedName("phoneNumber")
    private String phoneNumber;
    @SerializedName("createdDate")
    private Date createdDate;
    @SerializedName("categories")
    private List<Category> categories;
    @SerializedName("reviewStores")
    private List<ReviewStore> reviewStores;

    public Store(String storeId, String storeName) {
        this.storeId = storeId;
        this.storeName = storeName;
    }

    public Store(String storeId, String storeName, String location) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
    }

    public Store(String storeId, Account account, String storeName, String location, String phoneNumber, Date createdDate, List<Category> categories) {
        this.storeId = storeId;
        this.account = account;
        this.storeName = storeName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.categories = categories;
    }

    public Store(String storeId, String storeName, String location, Date createdDate) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.location = location;
        this.createdDate = createdDate;
    }

    public Store(String storeId, Account account, String storeName, String location, String phoneNumber, Date createdDate, List<Category> categories, List<ReviewStore> reviewStores) {
        this.storeId = storeId;
        this.account = account;
        this.storeName = storeName;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.createdDate = createdDate;
        this.categories = categories;
        this.reviewStores = reviewStores;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<ReviewStore> getReviewStores() {
        return reviewStores;
    }

    public void setReviewStores(List<ReviewStore> reviewStores) {
        this.reviewStores = reviewStores;
    }

    public String getStringCountFollow(int totalFollow) {
        String countFollow;
        if (totalFollow > 1000000) {
            if (totalFollow / 1000000 == 1) {
                countFollow = (totalFollow / 1000000) + "M";
            } else {
                DecimalFormat df = new DecimalFormat("##.0");
                String countTemp = String.valueOf(df.format(totalFollow / 1000000).replace(",", "."));
                countFollow = countTemp + "M";
            }
        } else if (totalFollow < 1000) {
            countFollow = String.valueOf(totalFollow);
        } else {
            countFollow = (totalFollow / 1000) + "K";
        }
        return countFollow;
    }

    public int getPositiveReview() {
        double positiveReview = 0;
        if (reviewStores.size() > 0) {
            int totalPositiveReview = getCountLevel(1) + getCountLevel(2);
            int totalReview = reviewStores.size();
            positiveReview = ((double) totalPositiveReview / totalReview) * 100;
        }
        return (int) positiveReview;
    }

    public int getCountLevel(int level) {
        int count = 0;
        if (reviewStores != null && reviewStores.size() > 0) {
            for (int i = 0; i < reviewStores.size(); i++) {
                if (reviewStores.get(i).getRatingLevel().getLevel() == level) {
                    count++;
                }
            }
        }
        return count;
    }

    public String getTimeInSystem() {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(createdDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(new Date());

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);

        return diffMonth == 0 ? ("Dưới 1 tháng") : ("Khoảng " + diffMonth + " tháng");
    }
}
