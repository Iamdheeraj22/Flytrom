package com.flytrom.learning.model.CouponModel;

import java.util.List;

public class CouponPojo {

    public int status;
    public String message;
    public List<CouponData> data;
    public String method;

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

    public List<CouponData> getData() {
        return data;
    }

    public void setData(List<CouponData> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
