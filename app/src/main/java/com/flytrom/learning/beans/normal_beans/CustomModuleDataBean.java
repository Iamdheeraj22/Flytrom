package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class CustomModuleDataBean {

    @SerializedName("number_of_questions")
    private String number_of_questions;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("questions_from")
    private String questions_from;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("totalSelectedSubjects")
    private String totalSelectedSubjects;
    @SerializedName("totalSelectedChapters")
    private String totalSelectedChapters;
    @SerializedName("mode_index")
    private String mode_index;

    public String getTotalSelectedSubjects() {
        return totalSelectedSubjects;
    }

    public void setTotalSelectedSubjects(String totalSelectedSubjects) {
        this.totalSelectedSubjects = totalSelectedSubjects;
    }

    public String getTotalSelectedChapters() {
        return totalSelectedChapters;
    }

    public void setTotalSelectedChapters(String totalSelectedChapters) {
        this.totalSelectedChapters = totalSelectedChapters;
    }

    public String getNumber_of_questions() {
        return number_of_questions;
    }

    public void setNumber_of_questions(String number_of_questions) {
        this.number_of_questions = number_of_questions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getQuestions_from() {
        return questions_from;
    }

    public void setQuestions_from(String questions_from) {
        this.questions_from = questions_from;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getMode_index() {
        return mode_index;
    }

    public void setMode_index(String mode_index) {
        this.mode_index = mode_index;
    }
}
