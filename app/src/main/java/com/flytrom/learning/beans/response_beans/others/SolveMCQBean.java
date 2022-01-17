package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SolveMCQBean implements Serializable {

    /**
     * question_id : 1
     * question_option_id : 3
     * was_correct : 1
     */

    @SerializedName("id")
    private String id;
    @SerializedName("test_id")
    private String test_id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("question_id")
    private int questionId;
    @SerializedName("question_option_id")
    private int questionOptionId;
    @SerializedName("was_correct")
    private int wasCorrect;
    @SerializedName("is_completed")
    private int is_completed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTest_id() {
        return test_id;
    }

    public void setTest_id(String test_id) {
        this.test_id = test_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionOptionId() {
        return questionOptionId;
    }

    public void setQuestionOptionId(int questionOptionId) {
        this.questionOptionId = questionOptionId;
    }

    public int getWasCorrect() {
        return wasCorrect;
    }

    public void setWasCorrect(int wasCorrect) {
        this.wasCorrect = wasCorrect;
    }

    public int getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }
}
