package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PlanDataBean implements Serializable {

    /**
     * id : 1
     * plan_name : ATPL ALL PLANS
     * has_meta : 1
     * amount : null
     * created_at : 2020-02-28 23:23:18
     * updated_at : 2020-02-28 23:23:18
     * status : Active
     * plan_metadata : [{"id":"1","plan_id":"1","title":"ATPL NAVIGATION","amount":"15000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"2","plan_id":"1","title":"ATPL NAVIGATION","amount":"10000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"3","plan_id":"1","title":"ATPL RADIO AIDS","amount":"15000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"4","plan_id":"1","title":"ATPL RADIO AIDS","amount":"10000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"5","plan_id":"1","title":"ATPL METEROLOGY","amount":"12000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"6","plan_id":"1","title":"ATPL METEROLOGY","amount":"9000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"7","plan_id":"1","title":"ATPL ALL SUBJECTS","amount":"33000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"8","plan_id":"1","title":"ATPL ALL SUBJECTS","amount":"26000","type":"Plan2","created_at":"2020-02-28 23:27:08"}]
     */

    @SerializedName("id")
    private int id;
    @SerializedName("plan_metadata_id")
    private int plan_metadata_id;
    @SerializedName("plan_name")
    private String planName;
    @SerializedName("has_meta")
    private String hasMeta;
    @SerializedName("amount")
    private String amount;
    @SerializedName("amount2")
    private String amount2;
    @SerializedName("old_amount")
    private String oldAmount;
    @SerializedName("old_amount2")
    private String oldAmount2;
    @SerializedName("subjects")
    private String subjects;
    @SerializedName("tagline")
    private String tagline;
    @SerializedName("number_of_subjects")
    private String number_of_subjects;
    @SerializedName("color")
    private String color;
    @SerializedName("validity")
    private String validity;
    @SerializedName("validity2")
    private String validity2;
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
    @SerializedName("image")
    private String image;
    @SerializedName("plan_metadata")
    private List<PlanMetadataBean> planMetadata;
    private boolean selected;
    private int index;

    private boolean colorSelect;

    public boolean isColorSelect() {
        return colorSelect;
    }

    public void setColorSelect(boolean colorSelect) {
        this.colorSelect = colorSelect;
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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getNumber_of_subjects() {
        return number_of_subjects;
    }

    public void setNumber_of_subjects(String number_of_subjects) {
        this.number_of_subjects = number_of_subjects;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlan_metadata_id() {
        return plan_metadata_id;
    }

    public void setPlan_metadata_id(int plan_metadata_id) {
        this.plan_metadata_id = plan_metadata_id;
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

    public String getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(String oldAmount) {
        this.oldAmount = oldAmount;
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

    public List<PlanMetadataBean> getPlanMetadata() {
        return planMetadata;
    }

    public void setPlanMetadata(List<PlanMetadataBean> planMetadata) {
        this.planMetadata = planMetadata;
    }

    public PlanTypesBean getPlanTypes() {
        return planTypes;
    }

    public void setPlanTypes(PlanTypesBean planTypes) {
        this.planTypes = planTypes;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
