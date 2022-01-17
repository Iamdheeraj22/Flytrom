package com.flytrom.learning.beans.response_beans.test_bean;

import com.flytrom.learning.beans.response_beans.others.MetadataBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTestsBean {

    /**
     * status : 200
     * message : All Tests
     * _metadata : {"page":1,"limit":1,"remaining_pages":0}
     * data : [{"id":"1","subject":"ATPL - Air Navigation","test_name":"Test1","type":"Free","questions":"1,2","created_at":"2020-02-18 17:03:13","updated_at":"2020-02-18 17:03:13","status":"Active","month_name":"February","year":"2020","average_rating":"0","user_rating":"0","is_completed":"0"}]
     * method : tests
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("_metadata")
    private MetadataBean metadata;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<TestBean> data;

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

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<TestBean> getData() {
        return data;
    }

    public void setData(List<TestBean> data) {
        this.data = data;
    }

}
