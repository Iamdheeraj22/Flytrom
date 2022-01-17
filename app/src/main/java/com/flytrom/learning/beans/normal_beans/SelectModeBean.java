package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class SelectModeBean {

    @SerializedName("mode")
    private String mode;
    @SerializedName("description")
    private String description;
    @SerializedName("index")
    private int index;
    @SerializedName("selected")
    private int selected;

    public SelectModeBean(String mode, String description, int index, int selected) {
        this.mode = mode;
        this.description = description;
        this.index = index;
        this.selected = selected;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int isSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
