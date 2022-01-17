package com.flytrom.learning.beans.response_beans.custom_module;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "CustomModule", primaryKeys = {"id"})
public class CustomModuleBean implements Serializable {
    /**
     * id : 4
     * number_of_questions : 100
     * difficulty : 0
     * questions_from : 3
     * subjects : 0
     * user_id : 5
     * has_answered : 0
     */

    @SerializedName("id")
    private int id;
    @SerializedName("number_of_questions")
    private String number_of_questions;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("mode")
    private String mode;
    @SerializedName("questions_from")
    private String questions_from;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("created_at")
    private String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    private String has_answered;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber_of_questions() {
        return number_of_questions;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHas_answered() {
        return has_answered;
    }

    public void setHas_answered(String has_answered) {
        this.has_answered = has_answered;
    }
}
