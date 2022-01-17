package com.flytrom.learning.beans.response_beans.custom_module;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetCustomModuleBean implements Serializable {


    /**
     * status : 200
     * message : All Custom Module
     * data : [{"id":"4","number_of_questions":"100","difficulty":"0","questions_from":"3","subjects":"0","user_id":"5","has_answered":"0"}]
     * method : get_custom_module
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<CustomModuleBean> data;

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

    public List<CustomModuleBean> getData() {
        return data;
    }

    public void setData(List<CustomModuleBean> data) {
        this.data = data;
    }

}
