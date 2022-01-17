package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

public class SubscribePlansBean {

    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("index")
    private int index;
    @SerializedName("selected")
    private boolean selected;

    public SubscribePlansBean(String name, String price, int index, boolean selected) {
        this.name = name;
        this.price = price;
        this.index = index;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
