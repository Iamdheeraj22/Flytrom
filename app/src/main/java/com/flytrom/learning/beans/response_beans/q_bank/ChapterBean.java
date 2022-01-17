package com.flytrom.learning.beans.response_beans.q_bank;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Chapters", primaryKeys = {"id"})
public class ChapterBean implements Serializable {
    /**
     * id : 1
     * type : Paid
     * question_category : ATPL - Air Navigation
     * title : The Earth, Latitude and Longitude
     * created_at : 2020-02-08 04:38:54
     * updated_at : null
     * status : Unattempted
     * has_rated : 1
     * average_rating : 4
     * total_mcqs : 11
     */

    private int position;
    @SerializedName("id")
    private int id;
    @SerializedName("main_item_position")
    private int main_item_position;
    @SerializedName("type")
    private String type;
    @SerializedName("question_category")
    private String question_category;
    @SerializedName("title")
    private String title;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("status")
    private String status;
    @SerializedName("has_rated")
    private String has_rated;
    @SerializedName("average_rating")
    private String average_rating;
    @SerializedName("total_mcqs")
    private String total_mcqs;
    @SerializedName("is_completed")
    private String is_completed;
    @SerializedName("saved_data")
    @TypeConverters(value = AllTypeConverters.class)
    private List<SolveMCQBean> savedData;
    @SerializedName("subject")
    private String subject;
    @SerializedName("test_name")
    private String test_name;
    @SerializedName("questions")
    private String questions;
    @SerializedName("month_name")
    private String month_name;
    @SerializedName("year")
    private String year;
    @SerializedName("user_rating")
    private String user_rating;
    @SerializedName("icon")
    private String icon;
    @SerializedName("is_downloaded")
    private String is_downloaded;
    @SerializedName("selected")
    private boolean selected;


    @SerializedName("progress")
    private String progress;

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    private String title_category;

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

    public int getMain_item_position() {
        return main_item_position;
    }

    public void setMain_item_position(int main_item_position) {
        this.main_item_position = main_item_position;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }

    public String getMonth_name() {
        return month_name;
    }

    public void setMonth_name(String month_name) {
        this.month_name = month_name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion_category() {
        return question_category;
    }

    public void setQuestion_category(String question_category) {
        this.question_category = question_category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHas_rated() {
        return has_rated;
    }

    public void setHas_rated(String has_rated) {
        this.has_rated = has_rated;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    public String getTotal_mcqs() {
        return total_mcqs;
    }

    public void setTotal_mcqs(String total_mcqs) {
        this.total_mcqs = total_mcqs;
    }

    public String getIs_downloaded() {
        return is_downloaded;
    }

    public void setIs_downloaded(String is_downloaded) {
        this.is_downloaded = is_downloaded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public List<SolveMCQBean> getSavedData() {
        return savedData;
    }

    public void setSavedData(List<SolveMCQBean> savedData) {
        this.savedData = savedData;
    }
}
