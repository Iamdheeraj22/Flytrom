package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

public class GetNewOrderIdBean {


    /**
     * status : 200
     * message : NEW ORDER ID
     * data : {"order_id":"ORDER_2"}
     * method : new_order_id
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static class DataBean {
        /**
         * order_id : ORDER_2
         */

        @SerializedName("order_id")
        private String orderId;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
