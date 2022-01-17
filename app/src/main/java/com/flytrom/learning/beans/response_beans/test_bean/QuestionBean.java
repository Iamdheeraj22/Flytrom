package com.flytrom.learning.beans.response_beans.test_bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Questions", primaryKeys = {"id", "testId", "customModuleId", "chapterId"})
public class QuestionBean implements Serializable {
    /**
     * id : 1
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
     * options : [{"id":"1","question_category":"ATPL - Air Navigation","question_id":"1","name":"3%","is_answer":"No","created_at":"2020-02-08 04:38:54","has_answered":"0"},{"id":"2","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.03%","is_answer":"No","created_at":"2020-02-08 04:38:54","has_answered":"0"},{"id":"3","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.3%","is_answer":"Yes","created_at":"2020-02-08 04:38:55","has_answered":"1"},{"id":"4","question_category":"ATPL - Air Navigation","question_id":"1","name":" 1/3000","is_answer":"No","created_at":"2020-02-08 04:38:55","has_answered":"0"}]
     */

    private int index;
    private boolean selected;
    @NonNull
    @SerializedName("id")
    private int id;
    @NonNull
    @SerializedName("test_id")
    private int testId;
    @NonNull
    @SerializedName("custom_module_id")
    private int customModuleId;
    @NonNull
    @SerializedName("chapter_id")
    private int chapterId;
    @SerializedName("tag_id")
    private String tagId;
    @SerializedName("question_category")
    private String questionCategory;
    @SerializedName("type")
    private String type;
    @SerializedName("b_type")
    private String b_type;
    @SerializedName("question_category_chapter")
    private String questionCategoryChapter;
    @SerializedName("question")
    private String question;
    @SerializedName("answer_explanation")
    private String answerExplanation;
    @SerializedName("question_file")
    private String questionFile;
    @SerializedName("answer_file")
    private String answerFile;
    @SerializedName("for_day")
    private String forDay;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @SerializedName("mcq_answered")
    private int mcqAnswered;
    @SerializedName("is_bookmarked")
    private String is_bookmarked;
    @SerializedName("options")
    @TypeConverters(value = {AllTypeConverters.class})
    private List<OptionsBean> options;
    @SerializedName("downloaded")
    private boolean downloaded;
    @SerializedName("selectedIndex")
    private int selectedIndex;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getCustomModuleId() {
        return customModuleId;
    }

    public void setCustomModuleId(int customModuleId) {
        this.customModuleId = customModuleId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getB_type() {
        return b_type;
    }

    public void setB_type(String b_type) {
        this.b_type = b_type;
    }

    public String getQuestionCategoryChapter() {
        return questionCategoryChapter;
    }

    public void setQuestionCategoryChapter(String questionCategoryChapter) {
        this.questionCategoryChapter = questionCategoryChapter;
    }

    public String getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(String questionCategory) {
        this.questionCategory = questionCategory;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerExplanation() {
        return answerExplanation;
    }

    public void setAnswerExplanation(String answerExplanation) {
        this.answerExplanation = answerExplanation;
    }

    public String getQuestionFile() {
        return questionFile;
    }

    public void setQuestionFile(String questionFile) {
        this.questionFile = questionFile;
    }

    public String getAnswerFile() {
        return answerFile;
    }

    public void setAnswerFile(String answerFile) {
        this.answerFile = answerFile;
    }

    public String getForDay() {
        return forDay;
    }

    public void setForDay(String forDay) {
        this.forDay = forDay;
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

    public String getIs_bookmarked() {
        return is_bookmarked;
    }

    public void setIs_bookmarked(String is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
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

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getMcqAnswered() {
        return mcqAnswered;
    }

    public void setMcqAnswered(int mcqAnswered) {
        this.mcqAnswered = mcqAnswered;
    }
}