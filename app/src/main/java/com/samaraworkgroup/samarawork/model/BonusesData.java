
package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BonusesData {

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("phone")
    @Expose
    private String phone;


    public BonusesData(Integer amount, String status, String phone) {
        this.amount = amount;
        this.status = status;
        this.phone = phone;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
