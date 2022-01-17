package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlanTypesBean implements Serializable {
    /**
     * id : 1
     * title : Plan1
     * videos : 1
     * question_bank : 0
     * tests : 0
     */

    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("videos")
    private int videos;
    @SerializedName("question_bank")
    private int questionBank;
    @SerializedName("tests")
    private int tests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVideos() {
        return videos;
    }

    public void setVideos(int videos) {
        this.videos = videos;
    }

    public int getQuestionBank() {
        return questionBank;
    }

    public void setQuestionBank(int questionBank) {
        this.questionBank = questionBank;
    }

    public int getTests() {
        return tests;
    }

    public void setTests(int tests) {
        this.tests = tests;
    }
}
