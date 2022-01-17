package com.flytrom.learning.beans.response_beans.auth;

import com.flytrom.learning.beans.response_beans.payment_beans.PlanTypesBean;
import com.google.gson.annotations.SerializedName;

public class PlanDetailsBean {
    /**
     * id : 3
     * plan_name : Radio Telephony (RTR)
     * has_meta : 0
     * amount : 20000
     * subjects : Radio Telephony (RTR)
     * plan_type : Plan 3
     * created_at : 2020-03-30 13:50:54
     * updated_at : 2020-03-30 13:50:54
     * status : Active
     * plan_types : {"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}
     */

    @SerializedName("id")
    private String id;
    @SerializedName("plan_metadata_id")
    private int plan_metadata_id;
    @SerializedName("plan_name")
    private String planName;
    @SerializedName("has_meta")
    private String hasMeta;
    @SerializedName("amount")
    private String amount;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("plan_type")
    private String planType;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    @SerializedName("status")
    private String status;
    @SerializedName("plan_types")
    private PlanTypesBean planTypes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getHasMeta() {
        return hasMeta;
    }

    public void setHasMeta(String hasMeta) {
        this.hasMeta = hasMeta;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PlanTypesBean getPlanTypes() {
        return planTypes;
    }

    public void setPlanTypes(PlanTypesBean planTypes) {
        this.planTypes = planTypes;
    }

}
