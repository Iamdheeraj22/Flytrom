package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class ResetBean {

    @SerializedName("index")
    private int index;
    @SerializedName("headline")
    private String headline;
    @SerializedName("description")
    private String description;

    public ResetBean(int index, String headline, String description) {
        this.index = index;
        this.headline = headline;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
