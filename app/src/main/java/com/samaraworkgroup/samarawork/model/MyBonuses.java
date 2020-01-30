package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyBonuses {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amountBonuses")
    @Expose
    private String  amountBonuses;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String  getAmountBonuses() {
        return amountBonuses;
    }

    public void setAmountBonuses(String  amountBonuses) {
        this.amountBonuses = amountBonuses;
    }
}
