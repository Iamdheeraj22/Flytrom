package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CouponBean implements Serializable{
    /**
     * id : 9
     * code : SAVE50
     * type : 3
     * discount : 5000
     * plan_id : 5
     * max_redemptions : 10
     * created_at : 2020-04-02 12:04:27
     * updated_at : 0000-00-00 00:00:00
     */

    @SerializedName("id")
    private String id;
    @SerializedName("code")
    private String code;
    @SerializedName("type")
    private String type;
    @SerializedName("discount")
    private int discount;
    @SerializedName("plan_id")
    private String planId;
    @SerializedName("max_redemptions")
    private String maxRedemptions;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getMaxRedemptions() {
        return maxRedemptions;
    }

    public void setMaxRedemptions(String maxRedemptions) {
        this.maxRedemptions = maxRedemptions;
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

}
