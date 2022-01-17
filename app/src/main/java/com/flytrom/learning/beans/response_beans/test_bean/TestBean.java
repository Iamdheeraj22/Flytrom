package com.flytrom.learning.beans.response_beans.test_bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Tests", primaryKeys = {"id"})
public class TestBean implements Serializable {
    /**
     * id : 1
     * subject : ATPL - Air Navigation
     * test_name : Test1
     * type : Free
     * questions : 1,2
     * created_at : 2020-02-18 17:03:13
     * updated_at : 2020-02-18 17:03:13
     * status : Active
     * month_name : February
     * year : 2020
     * average_rating : 0
     * user_rating : 0
     * is_completed : 0
     */


    private int position;
    @NonNull
    @SerializedName("id")
    private int id;
    @SerializedName("subject")
    private String subject;
    @SerializedName("test_name")
    private String testName;
    @SerializedName("type")
    private String type;
    @SerializedName("questions")
    private String questions;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @SerializedName("total_questions")
    private String totalQuestions;
    @SerializedName("month_name")
    private String monthName;
    @SerializedName("year")
    private String year;
    @SerializedName("average_rating")
    private String averageRating;
    @SerializedName("user_rating")
    private String userRating;
    @SerializedName("is_completed")
    private String isCompleted;
    @SerializedName("saved_data")
    @TypeConverters(value = AllTypeConverters.class)
    private List<SolveMCQBean> savedData;
    @SerializedName("icon")
    private String icon;
    @SerializedName("is_downloaded")
    private String is_downloaded;

    @SerializedName("title_category")
    private String title_category;

    @SerializedName("progress")
    private String progress;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    //    @SerializedName("title_category")
//    private String main_test_percentage;
//
//    public String getMain_test_percentage() {
//        return main_test_percentage;
//    }
//
//    public void setMain_test_percentage(String main_test_percentage) {
//        this.main_test_percentage = main_test_percentage;
//    }

    public String getTitle_category() {
        return title_category;
    }

    public void setTitle_category(String title_category) {
        this.title_category = title_category;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(String totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }

    public List<SolveMCQBean> getSavedData() {
        return savedData;
    }

    public void setSavedData(List<SolveMCQBean> savedData) {
        this.savedData = savedData;
    }

    public String getIs_downloaded() {
        return is_downloaded;
    }

    public void setIs_downloaded(String is_downloaded) {
        this.is_downloaded = is_downloaded;
    }
}
