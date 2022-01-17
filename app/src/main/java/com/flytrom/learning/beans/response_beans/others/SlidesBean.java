package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SlidesBean implements Serializable {

    /**
     * id : 4
     * video_id : 4
     * file : video_slides/2020-02/20200208_EP-2426-1581140592.jpg
     */

    @SerializedName("id")
    private String id;
    @SerializedName("video_id")
    private String videoId;
    @SerializedName("file")
    private String file;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
