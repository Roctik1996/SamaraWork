package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageList {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("date")
    @Expose
    private Long date;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("is_read")
    @Expose
    private Boolean isRead;

    @SerializedName("image")
    @Expose
    private String photo;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
