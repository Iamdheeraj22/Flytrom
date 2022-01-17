package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopicsBean implements Serializable {

    /**
     * id : 1
     * video_id : 4
     * topic : First TopicBean
     * duration : 1
     * created_on : 2020-02-08 05:16:18
     * status : Active
     */

    @SerializedName("id")
    private String id;
    @SerializedName("video_id")
    private String videoId;
    @SerializedName("topic")
    private String topic;
    @SerializedName("minutes")
    private String minutes;
    @SerializedName("seconds")
    private String seconds;
    @SerializedName("duration")
    private String duration;
    @SerializedName("created_on")
    private String createdOn;
    @SerializedName("status")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
