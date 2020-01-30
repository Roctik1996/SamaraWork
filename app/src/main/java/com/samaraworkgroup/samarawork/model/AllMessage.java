package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllMessage {
    @SerializedName("message_list")
    @Expose
    private ArrayList<MessageList> messageList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<MessageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(ArrayList<MessageList> messageList) {
        this.messageList = messageList;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
