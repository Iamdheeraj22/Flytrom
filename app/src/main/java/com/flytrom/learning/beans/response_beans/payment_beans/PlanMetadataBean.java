package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlanMetadataBean implements Serializable {

    /**
     * id : 1
     * plan_id : 1
     * title : ATPL NAVIGATION
     * amount : 15000
     * type : Plan1
     * created_at : 2020-02-28 23:27:08
     */

    @SerializedName("id")
    private String id;
    @SerializedName("plan_id")
    private String planId;
    @SerializedName("title")
    private String title;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("amount")
    private String amount;
    @SerializedName("old_amount")
    private String oldAmount;
    @SerializedName("amount2")
    private String amount2;
    @SerializedName("old_amount2")
    private String oldAmount2;
    @SerializedName("type")
    private String type;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("validity")
    private String validity;
    @SerializedName("validity2")
    private String validity2;
    @SerializedName("plan_types")
    private PlanTypesBean planTypes;

    private String percentage;
    private String percentage2;

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidity2() {
        return validity2;
    }

    public void setValidity2(String validity2) {
        this.validity2 = validity2;
    }

    public String getAmount2() {
        return amount2;
    }

    public void setAmount2(String amount2) {
        this.amount2 = amount2;
    }

    public String getOldAmount2() {
        return oldAmount2;
    }

    public void setOldAmount2(String oldAmount2) {
        this.oldAmount2 = oldAmount2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(String oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public PlanTypesBean getPlanTypes() {
        return planTypes;
    }

    public void setPlanTypes(PlanTypesBean planTypes) {
        this.planTypes = planTypes;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getPercentage2() {
        return percentage2;
    }

    public void setPercentage2(String percentage2) {
        this.percentage2 = percentage2;
    }
}
