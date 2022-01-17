package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUrlsBean {

    /**
     * status : 200
     * message : All Urls
     * data : [{"id":"1","type":"PILOT APP","url":"http://google.com"},{"id":"2","type":"PILOT TRAINING","url":"http://twitter.com"}]
     * method : get_urls
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<GetUrlDataBean> data;

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

    public List<GetUrlDataBean> getData() {
        return data;
    }

    public void setData(List<GetUrlDataBean> data) {
        this.data = data;
    }

}
