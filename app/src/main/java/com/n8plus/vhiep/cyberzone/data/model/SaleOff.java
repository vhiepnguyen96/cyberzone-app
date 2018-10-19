package com.n8plus.vhiep.cyberzone.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class SaleOff implements Serializable{
    @SerializedName("_id")
    private String id;
    @SerializedName("discount")
    private int discount;
    @SerializedName("dateStart")
    private Date dateStart;
    @SerializedName("dateEnd")
    private Date dateEnd;

    public SaleOff(String id, int discount) {
        this.id = id;
        this.discount = discount;
    }

    public SaleOff(String id, int discount, Date dateStart, Date dateEnd) {
        this.id = id;
        this.discount = discount;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }
}
