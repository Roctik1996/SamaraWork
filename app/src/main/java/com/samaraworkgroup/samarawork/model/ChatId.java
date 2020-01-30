package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatId {

    @SerializedName("chat_id")
    @Expose
    private Integer chat_id;

    public Integer getChat_id() {
        return chat_id;
    }

    public void setChat_id(Integer chat_id) {
        this.chat_id = chat_id;
    }

    public ChatId(Integer chat_id) {
        this.chat_id = chat_id;
    }
}
