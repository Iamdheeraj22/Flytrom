package com.flytrom.learning.beans.response_beans.random_question;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "RandomQuestion", primaryKeys = {"id"})
public class RandomQuestionBean {
    /**
     * id : 1
     * question_category : Radio Telephony
     * question_category_chapter : Aeronauticals Stations
     * question : What does FAA stand for?
     * level : Easy
     * answer_explanation : null
     * question_file : null
     * answer_file : null
     * for_day : 2020-02-03
     * created_at : 2020-02-02 16:22:18
     * updated_at : null
     * status : Active
     * has_answered : 0
     * options : [{"id":"1","question_category":"Radio Telephony","question_id":"1","name":"Federal Avionics Administration","is_answer":"Yes","created_at":"2020-02-02 16:22:19"},{"id":"2","question_category":"Radio Telephony","question_id":"1","name":" Federal Aviation Administration","is_answer":"No","created_at":"2020-02-02 16:22:19"},{"id":"3","question_category":"Radio Telephony","question_id":"1","name":"Fedex Aircraft Administration","is_answer":"No","created_at":"2020-02-02 16:22:19"}]
     */

    @NonNull
    @SerializedName("id")
    private int id;
    @SerializedName("tag_id")
    private String tagId;
    @SerializedName("question_category")
    private String question_category;
    @SerializedName("question_category_chapter")
    private String question_category_chapter;
    @SerializedName("question")
    private String question;
    @SerializedName("level")
    private String level;
    @SerializedName("answer_explanation")
    private String answer_explanation;
    @SerializedName("question_file")
    private String question_file;
    @SerializedName("answer_file")
    private String answer_file;
    @SerializedName("for_day")
    private String for_day;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("status")
    private String status;
    @SerializedName("has_answered")
    private String has_answered;
    @SerializedName("headline")
    private String headline;
    @SerializedName("description")
    private String description;
    @SerializedName("options")
    @TypeConverters(value = {AllTypeConverters.class})
    private List<OptionsBean> options;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getQuestion_category() {
        return question_category;
    }

    public void setQuestion_category(String question_category) {
        this.question_category = question_category;
    }

    public String getQuestion_category_chapter() {
        return question_category_chapter;
    }

    public void setQuestion_category_chapter(String question_category_chapter) {
        this.question_category_chapter = question_category_chapter;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAnswer_explanation() {
        return answer_explanation;
    }

    public void setAnswer_explanation(String answer_explanation) {
        this.answer_explanation = answer_explanation;
    }

    public String getQuestion_file() {
        return question_file;
    }

    public void setQuestion_file(String question_file) {
        this.question_file = question_file;
    }

    public String getAnswer_file() {
        return answer_file;
    }

    public void setAnswer_file(String answer_file) {
        this.answer_file = answer_file;
    }

    public String getFor_day() {
        return for_day;
    }

    public void setFor_day(String for_day) {
        this.for_day = for_day;
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


    public String getHas_answered() {
        return has_answered;
    }

    public void setHas_answered(String has_answered) {
        this.has_answered = has_answered;
    }

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}