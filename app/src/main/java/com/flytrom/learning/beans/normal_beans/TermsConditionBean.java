package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class TermsConditionBean {

    @SerializedName("index")
    private int index;
    @SerializedName("type")
    private String type;
    @SerializedName("heading")
    private String heading;
    @SerializedName("message")
    private String message;
    @SerializedName("selected")
    private boolean selected;

    public TermsConditionBean(int index, String type, String heading, String message, boolean selected) {
        this.type = type;
        this.index = index;
        this.heading = heading;
        this.message = message;
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
