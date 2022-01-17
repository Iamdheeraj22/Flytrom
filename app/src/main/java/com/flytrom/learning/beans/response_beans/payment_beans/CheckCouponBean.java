package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckCouponBean implements Serializable {

    /**
     * status : 200
     * message : Coupon Applied
     * data : {"id":"9","code":"SAVE50","type":"3","discount":"5000","plan_id":"5","max_redemptions":"10","created_at":"2020-04-02 12:04:27","updated_at":"0000-00-00 00:00:00"}
     * method : check_coupon
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private CouponBean data;
    @SerializedName("method")
    private String method;

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

    public CouponBean getData() {
        return data;
    }

    public void setData(CouponBean data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
