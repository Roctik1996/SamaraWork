package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllChat {
    @SerializedName("chat_list")
    @Expose
    private ArrayList<ChatList> chatList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<ChatList> chatList) {
        this.chatList = chatList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
