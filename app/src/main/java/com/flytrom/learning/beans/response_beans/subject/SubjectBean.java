package com.flytrom.learning.beans.response_beans.subject;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Subjects", primaryKeys = {"id", "orderType"})
public class SubjectBean implements Serializable {
    /**
     * id : 1
     * title : ATPL - Air Navigation
     * icon : null
     * created_at : 2020-02-08 04:38:54
     * updated_at : null
     * status : Active
     * total_chapter_completed : 0
     * total_chapters : 1
     * total_video_completed : 0
     * total_videos : 4
     * total_test_completed : 0
     * total_tests : 1
     */

    @SerializedName("index")
    private int index;
    @SerializedName("selected")
    private boolean selected;
    @SerializedName("expanded")
    private boolean expanded;
    @SerializedName("video_category")
    private String video_category;
    @NonNull
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("icon")
    private String icon;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @NonNull
    @SerializedName("order_type")
    private String orderType;
    @SerializedName("total_chapter_completed")
    private String totalChapterCompleted;
    @SerializedName("total_chapters")
    private String totalChapters;
    @SerializedName("qb_completion_percent")
    private String totalVideoCompleted;
    @SerializedName("total_videos")
    private String totalVideos;
    @SerializedName("test_completion_percent")
    private String totalTestCompleted;

    @SerializedName("percentage")
    private String percentage = "0";

    @SerializedName("video_completion_percent")
    private String video_completion_percent = "0";

    @SerializedName("percentage_qbank")
    private int percentage_qbank;

    @SerializedName("percentage_test")
    private String percentage_test = "0";

    @SerializedName("total_tests")
    private String totalTests;
    @TypeConverters(value = {AllTypeConverters.class})
    private List<ChapterBean> chapters;
    @SerializedName("locked")
    private boolean locked;
    @SerializedName("type")
    private String type;

    private String title_category;


    public String getVideo_completion_percent() {
        return video_completion_percent;
    }

    public void setVideo_completion_percent(String video_completion_percent) {
        this.video_completion_percent = video_completion_percent;
    }

    public String getTitle_category() {
        return title_category;
    }

    public void setTitle_category(String title_category) {
        this.title_category = title_category;
    }

    public int getPercentage_qbank() {
        return percentage_qbank;
    }

    public void setPercentage_qbank(int percentage_qbank) {
        this.percentage_qbank = percentage_qbank;
    }

    public String getPercentage_test() {
        return percentage_test;
    }

    public void setPercentage_test(String percentage_test) {
        this.percentage_test = percentage_test;
    }

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

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getVideo_category() {
        return video_category;
    }

    public void setVideo_category(String video_category) {
        this.video_category = video_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTotalChapterCompleted() {
        return totalChapterCompleted;
    }

    public void setTotalChapterCompleted(String totalChapterCompleted) {
        this.totalChapterCompleted = totalChapterCompleted;
    }

    public String getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(String totalChapters) {
        this.totalChapters = totalChapters;
    }

    public String getTotalVideoCompleted() {
        return totalVideoCompleted;
    }

    public void setTotalVideoCompleted(String totalVideoCompleted) {
        this.totalVideoCompleted = totalVideoCompleted;
    }

    public String getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(String totalVideos) {
        this.totalVideos = totalVideos;
    }

    public String getTotalTestCompleted() {
        return totalTestCompleted;
    }

    public void setTotalTestCompleted(String totalTestCompleted) {
        this.totalTestCompleted = totalTestCompleted;
    }

    public String getPercentage() {
        return percentage;
    }

    public Float getPercentageFloat() {
        return Float.valueOf(percentage);
    }

    public void setPercentageFloat(Float percentage) {
        this.percentage = percentage.toString();
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(String totalTests) {
        this.totalTests = totalTests;
    }

    public List<ChapterBean> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterBean> chapters) {
        this.chapters = chapters;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
