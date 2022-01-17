package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class SideMenuBean {

    @SerializedName("name")
    private String name;
    @SerializedName("selected")
    private boolean selected;
    @SerializedName("tag")
    private int tag;
    @SerializedName("icon")
    private int icon;

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public SideMenuBean(String name, boolean selected, int tag, int icon) {
        this.name = name;
        this.selected = selected;
        this.tag = tag;
        this.icon = icon;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
