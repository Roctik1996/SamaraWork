package com.samaraworkgroup.samarawork.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("object_address")
    @Expose
    private String objectAddress;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("image_question")
    @Expose
    private String  imageQuestion;
    @SerializedName("image_answer")
    @Expose
    private String  imageAnswer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getObjectAddress() {
        return objectAddress;
    }

    public void setObjectAddress(String objectAddress) {
        this.objectAddress = objectAddress;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String  getImageQuestion() {
        return imageQuestion;
    }

    public void setImageQuestion(String  imageQuestion) {
        this.imageQuestion = imageQuestion;
    }

    public String  getImageAnswer() {
        return imageAnswer;
    }

    public void setImageAnswer(String  imageAnswer) {
        this.imageAnswer = imageAnswer;
    }
}
