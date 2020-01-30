package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Admin {
    @SerializedName("questions")
    @Expose
    private ArrayList<Question> questions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
