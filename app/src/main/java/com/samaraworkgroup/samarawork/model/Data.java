package com.samaraworkgroup.samarawork.model;

public class Data {
    private String phone;
    private String money;
    private String status;

    public Data(String phone, String money, String status) {
        this.phone = phone;
        this.money = money;
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
