package com.flytrom.learning.beans.response_beans.videos;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.flytrom.learning.beans.response_beans.others.TopicsBean;
import com.flytrom.learning.beans.response_beans.others.SlidesBean;
import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "Videos", primaryKeys = {"id", "type"})
public class VideoBean implements Serializable {

    /**
     * id : 4
     * videocypherid : 6bd0ba90cb3734e4aa754508a7f7b215
     * type : Paid
     * title : Fourth Video
     * category : ATPL - Air Navigation
     * bucket : vdo-ap-southeast
     * videokey : orig/5e3e436710935
     * etag : "1f3d13e2110765f99ee8cf9dd124afd0"
     * notes : notes/2020-02/20200228_EP-1612-1582916033.pdf
     * length : 46
     * is_downloaded : No
     * created_at : 2020-02-08 05:15:17
     * updated_at : null
     * has_rated : 0
     * average_rating : 3
     * status : Unattempted
     * video_link : https://player.vdocipher.com/playerAssets/1.x/vdo/embed/index.html#otp=20160313versASE323pv8q2BE7gejjwsrUd7C2O0jj9oDEH4JN0swRUQlJuypdBM&playbackInfo=eyJ2aWRlb0lkIjoiNmJkMGJhOTBjYjM3MzRlNGFhNzU0NTA4YTdmN2IyMTUifQ==
     * topics : [{"id":"1","video_id":"4","topic":"First TopicBean","duration":"1","created_on":"2020-02-08 05:16:18","status":"Active"},{"id":"2","video_id":"4","topic":"Second TopicBean","duration":"2","created_on":"2020-02-08 05:16:31","status":"Active"}]
     * slides : [{"id":"4","video_id":"4","file":"video_slides/2020-02/20200208_EP-2426-1581140592.jpg"},{"id":"5","video_id":"4","file":"video_slides/2020-02/20200208_EP-4703-1581140606.png"}]
     * otp : 20160313versASE323pv8q2BE7gejjwsrUd7C2O0jj9oDEH4JN0swRUQlJuypdBM
     * playbackInfo : eyJ2aWRlb0lkIjoiNmJkMGJhOTBjYjM3MzRlNGFhNzU0NTA4YTdmN2IyMTUifQ==
     */

    private int position;
    @NonNull
    @SerializedName("id")
    private int id;
    @SerializedName("videocypherid")
    private String videocypherid;
    @NonNull
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("icon")
    private String icon;
    @SerializedName("video_banner")
    private String videoBanner;
    @SerializedName("description")
    private String description;
    @SerializedName("category")
    private String category;
    @SerializedName("bucket")
    private String bucket;
    @SerializedName("videokey")
    private String videokey;
    @SerializedName("etag")
    private String etag;
    @SerializedName("notes")
    private String notes;
    @SerializedName("length")
    private int length;
    @SerializedName("is_downloaded")
    private String isDownloaded;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("has_rated")
    private String hasRated;
    @SerializedName("average_rating")
    private String averageRating;
    @SerializedName("status")
    private String status;
    @SerializedName("seconds")
    private int seconds;
    @SerializedName("vimeo_video_uri")
    private String videoLink;
    @SerializedName("otp")
    private String otp;
    @SerializedName("playbackInfo")
    private String playbackInfo;
    @TypeConverters(value = AllTypeConverters.class)
    @SerializedName("topics")
    private List<TopicsBean> topics;
    @TypeConverters(value = AllTypeConverters.class)
    @SerializedName("slides")
    private List<SlidesBean> slides;
    @SerializedName("downloadStatus")
    private String downloadStatus;

    @SerializedName("video_durations")
    private String video_durations;

    public String getVideo_durations() {
        return video_durations;
    }

    public void setVideo_durations(String video_durations) {
        this.video_durations = video_durations;
    }

    private String title_category;

    private int durationpercentage;

    public int getDurationpercentage() {
        return durationpercentage;
    }

    public void setDurationpercentage(int durationpercentage) {
        this.durationpercentage = durationpercentage;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideocypherid() {
        return videocypherid;
    }

    public void setVideocypherid(String videocypherid) {
        this.videocypherid = videocypherid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getVideoBanner() {
        return videoBanner;
    }

    public void setVideoBanner(String videoBanner) {
        this.videoBanner = videoBanner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getVideokey() {
        return videokey;
    }

    public void setVideokey(String videokey) {
        this.videokey = videokey;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getIsDownloaded() {
        return isDownloaded;
    }

    public void setIsDownloaded(String isDownloaded) {
        this.isDownloaded = isDownloaded;
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

    public String getHasRated() {
        return hasRated;
    }

    public void setHasRated(String hasRated) {
        this.hasRated = hasRated;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getPlaybackInfo() {
        return playbackInfo;
    }

    public void setPlaybackInfo(String playbackInfo) {
        this.playbackInfo = playbackInfo;
    }

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public List<SlidesBean> getSlides() {
        return slides;
    }

    public void setSlides(List<SlidesBean> slides) {
        this.slides = slides;
    }

    public String getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(String downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

}
