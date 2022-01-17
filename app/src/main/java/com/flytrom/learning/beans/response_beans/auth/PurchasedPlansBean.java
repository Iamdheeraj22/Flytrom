package com.flytrom.learning.beans.response_beans.auth;

import com.flytrom.learning.beans.response_beans.payment_beans.CouponAppliedBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanMetadataBean;
import com.google.gson.annotations.SerializedName;

public class PurchasedPlansBean {
    /**
     * id : 3
     * user_id : 15
     * plan_id : 3
     * plan_metadata_id : 0
     * referenceId : 294763
     * paymentMode : WALLET
     * txStatus : SUCCESS
     * txTime : 2020-04-04 16:35:39
     * created_at : 2020-04-04 11:44:02
     * has_expired : 1
     * plan_details : {"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}}
     * user_coupon_applied : null
     */

    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("plan_id")
    private int planId;
    @SerializedName("plan_metadata_id")
    private String planMetadataId;
    @SerializedName("referenceId")
    private String referenceId;
    @SerializedName("paymentMode")
    private String paymentMode;
    @SerializedName("invoice_pdf")
    private String invoice_pdf;
    @SerializedName("txStatus")
    private String txStatus;
    @SerializedName("txTime")
    private String txTime;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("has_expired")
    private String hasExpired;
    @SerializedName("plan_details")
    private PlanDetailsBean planDetails;
    @SerializedName("plan_meta_details")
    private PlanMetadataBean planMetadataBean;
    @SerializedName("user_coupon_applied")
    private CouponAppliedBean userCouponApplied;
    @SerializedName("plan_expiry")
    private String plan_expiry;

    @SerializedName("validity")
    private String validity;

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getPlan_expiry() {
        return plan_expiry;
    }

    public void setPlan_expiry(String plan_expiry) {
        this.plan_expiry = plan_expiry;
    }

    public String getInvoice_pdf() {
        return invoice_pdf;
    }

    public void setInvoice_pdf(String invoice_pdf) {
        this.invoice_pdf = invoice_pdf;
    }

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

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanMetadataId() {
        return planMetadataId;
    }

    public void setPlanMetadataId(String planMetadataId) {
        this.planMetadataId = planMetadataId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public String getTxTime() {
        return txTime;
    }

    public void setTxTime(String txTime) {
        this.txTime = txTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getHasExpired() {
        return hasExpired;
    }

    public void setHasExpired(String hasExpired) {
        this.hasExpired = hasExpired;
    }

    public PlanDetailsBean getPlanDetails() {
        return planDetails;
    }

    public void setPlanDetails(PlanDetailsBean planDetails) {
        this.planDetails = planDetails;
    }

    public PlanMetadataBean getPlanMetadataBean() {
        return planMetadataBean;
    }

    public void setPlanMetadataBean(PlanMetadataBean planMetadataBean) {
        this.planMetadataBean = planMetadataBean;
    }

    public CouponAppliedBean getUserCouponApplied() {
        return userCouponApplied;
    }

    public void setUserCouponApplied(CouponAppliedBean userCouponApplied) {
        this.userCouponApplied = userCouponApplied;
    }
}
