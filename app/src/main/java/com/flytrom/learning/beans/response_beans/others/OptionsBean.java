package com.flytrom.learning.beans.response_beans.others;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "Options")
public class OptionsBean implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("index")
    private int index;
    @SerializedName("selectedIndex")
    private int selectedIndex;
    @SerializedName("selected")
    private boolean selected;
    @SerializedName("id")
    private int id;
    @SerializedName("question_category")
    private String questionCategory;
    @SerializedName("question_id")
    private int questionId;
    @SerializedName("name")
    private String name;
    @SerializedName("is_answer")
    private String isAnswer;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("has_answered")
    private String hasAnswered;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getHasAnswered() {
        return hasAnswered;
    }

    public void setHasAnswered(String hasAnswered) {
        this.hasAnswered = hasAnswered;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
