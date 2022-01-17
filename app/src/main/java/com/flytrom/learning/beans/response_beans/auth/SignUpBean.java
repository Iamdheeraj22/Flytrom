package com.flytrom.learning.beans.response_beans.auth;

import com.google.gson.annotations.SerializedName;

public class SignUpBean {
    /**
     * status : 200
     * message : Activation link has been sent to email.
     * method : signup
     */
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
