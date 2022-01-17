package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

public class CouponAppliedBean {
    /**
     * id : 10
     * user_id : 15
     * plan_id : 7
     * coupon_type : 2
     * discount : 30
     */

    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("plan_id")
    private String planId;
    @SerializedName("coupon_type")
    private String couponType;
    @SerializedName("discount")
    private int discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
