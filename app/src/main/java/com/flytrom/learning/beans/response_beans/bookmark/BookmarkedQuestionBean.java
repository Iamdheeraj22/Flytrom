package com.flytrom.learning.beans.response_beans.bookmark;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "BookmarkedQuestions", primaryKeys = {"id"})
public class BookmarkedQuestionBean implements Serializable {
    /**
     * id : 1
     * user_id : 5
     * question_id : 1
     * tag_id : NA1-0001
     * question_category : ATPL - Air Navigation
     * question_category_chapter : The Earth, Latitude and Longitude
     * question : The approximate compression of the Earth is
     * level : Easy
     * answer_explanation : The shape of the Earth is a sphere. This shape developed when the Earth formed from a gas-cloud as the spin
     * of the cloud caused higher centrifugal forces at the equatorial region than in regions nearer
     * the poles. The flattening is called
     * compression and in the case of the Earth is approximately
     * 0.3% (1/300th). The Earth’s polar diameter is or 23 nautical miles or 43 km
     * less than its equatorial diameter.
     * question_file :
     * answer_file : https://photos.app.goo.gl/HMKxhDTmpLfrCp4Z7
     * for_day : null
     * created_at : 2020-02-08 04:38:54
     * updated_at : null
     * status : Active
     * options : [{"id":"1","question_category":"ATPL - Air Navigation","question_id":"1","name":"3%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"2","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.03%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"3","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.3%","is_answer":"Yes","created_at":"2020-02-08 04:38:55"},{"id":"4","question_category":"ATPL - Air Navigation","question_id":"1","name":" 1/3000","is_answer":"No","created_at":"2020-02-08 04:38:55"}]
     */

    private int index;
    private boolean selected;
    @NonNull
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("question_id")
    private String question_id;
    @SerializedName("tag_id")
    private String tag_id;
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
    @SerializedName("options")
    @TypeConverters(value = {AllTypeConverters.class})
    private List<OptionsBean> options;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
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

    public List<OptionsBean> getOptions() {
        return options;
    }

    public void setOptions(List<OptionsBean> options) {
        this.options = options;
    }

}
