package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("last_message")
    @Expose
    private Long lastMessage;

    @SerializedName("is_read")
    @Expose
    private Boolean isRead;

    public ChatList(Integer id, String name, Long lastMessage, Boolean isRead) {
        this.id = id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.isRead = isRead;
    }

    public Long getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Long lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
