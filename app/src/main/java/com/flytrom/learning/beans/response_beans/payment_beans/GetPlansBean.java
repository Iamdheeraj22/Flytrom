package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetPlansBean {

    /**
     * status : 200
     * message : All Plans
     * data : [{"id":"1","plan_name":"ATPL ALL PLANS","has_meta":"1","amount":null,"created_at":"2020-02-28 23:23:18","updated_at":"2020-02-28 23:23:18","status":"Active","plan_metadata":[{"id":"1","plan_id":"1","title":"ATPL NAVIGATION","amount":"15000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"2","plan_id":"1","title":"ATPL NAVIGATION","amount":"10000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"3","plan_id":"1","title":"ATPL RADIO AIDS","amount":"15000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"4","plan_id":"1","title":"ATPL RADIO AIDS","amount":"10000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"5","plan_id":"1","title":"ATPL METEROLOGY","amount":"12000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"6","plan_id":"1","title":"ATPL METEROLOGY","amount":"9000","type":"Plan2","created_at":"2020-02-28 23:27:08"},{"id":"7","plan_id":"1","title":"ATPL ALL SUBJECTS","amount":"33000","type":"Plan1","created_at":"2020-02-28 23:27:08"},{"id":"8","plan_id":"1","title":"ATPL ALL SUBJECTS","amount":"26000","type":"Plan2","created_at":"2020-02-28 23:27:08"}]}]
     * method : get_plans
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<PlanDataBean> data;
    @SerializedName("plan_types")
    private List<PlanTypesBean> planTypes;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<PlanDataBean> getData() {
        return data;
    }

    public void setData(List<PlanDataBean> data) {
        this.data = data;
    }

    public List<PlanTypesBean> getPlanTypes() {
        return planTypes;
    }

    public void setPlanTypes(List<PlanTypesBean> planTypes) {
        this.planTypes = planTypes;
    }
}
