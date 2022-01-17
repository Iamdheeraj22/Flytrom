package com.flytrom.learning.beans.normal_beans;

import android.util.Pair;

import com.google.gson.annotations.SerializedName;

public class DownloadVideoTrackBean {

    @SerializedName("name")
    private String name;
    @SerializedName("selected")
    private boolean selected;
    @SerializedName("tag")
    private Pair<Integer, Integer> tag;

    public DownloadVideoTrackBean(String name, boolean selected,Pair<Integer,Integer> tag) {
        this.name = name;
        this.selected = selected;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Pair<Integer, Integer> getTag() {
        return tag;
    }

    public void setTag(Pair<Integer, Integer> tag) {
        this.tag = tag;
    }
}
