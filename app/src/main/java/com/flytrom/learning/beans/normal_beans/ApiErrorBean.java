package com.flytrom.learning.beans.normal_beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 7/31/2018.
 */

public class ApiErrorBean {

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public String getMethod() {
        return method;
    }

}
