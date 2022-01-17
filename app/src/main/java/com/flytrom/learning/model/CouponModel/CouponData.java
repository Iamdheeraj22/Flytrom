package com.flytrom.learning.model.CouponModel;

public class CouponData {
    public String id;
    public String region_id;
    public String code;
    public String type;
    public String discount;
    public String plan_id;
    public String expiry_date;
    public String max_redemptions;
    public String price_condition;
    public String created_at;
    public String updated_at;
    public String status;
    public PlanDetails plan_details;

    public PlanDetails getPlan_details() {
        return plan_details;
    }

    public void setPlan_details(PlanDetails plan_details) {
        this.plan_details = plan_details;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
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

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getMax_redemptions() {
        return max_redemptions;
    }

    public void setMax_redemptions(String max_redemptions) {
        this.max_redemptions = max_redemptions;
    }

    public String getPrice_condition() {
        return price_condition;
    }

    public void setPrice_condition(String price_condition) {
        this.price_condition = price_condition;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
