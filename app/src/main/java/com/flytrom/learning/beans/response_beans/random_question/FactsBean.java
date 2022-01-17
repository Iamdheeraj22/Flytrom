package com.flytrom.learning.beans.response_beans.random_question;

import com.google.gson.annotations.SerializedName;

public class FactsBean {

    /**
     * id : 2
     * headline : Wingspan
     * description : The wing-span of the A380 is longer than the aircraft itself. Wingspan is 80m, the length is 72.7m
     * created_at : 2020-03-29 17:54:30
     */

    @SerializedName("id")
    private String id;
    @SerializedName("headline")
    private String headline;
    @SerializedName("description")
    private String description;
    @SerializedName("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
