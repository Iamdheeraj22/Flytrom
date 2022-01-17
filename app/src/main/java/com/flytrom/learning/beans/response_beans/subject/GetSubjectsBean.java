package com.flytrom.learning.beans.response_beans.subject;

import com.flytrom.learning.beans.response_beans.others.MetadataBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSubjectsBean {

    /**
     * status : 200
     * message : All Question Categories
     * _metadata : {"page":1,"limit":10,"remaining_pages":0}
     * data : [{"id":"1","title":"ATPL - Air Navigation","icon":null,"created_at":"2020-02-08 04:38:54","updated_at":null,"status":"Active","total_chapter_completed":"0","total_chapters":"1","total_video_completed":"0","total_videos":"4","total_test_completed":"0","total_tests":"1"}]
     * bookmark_counter : 1
     * method : question_bank_categories
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("_metadata")
    private MetadataBean metadata;
    @SerializedName("bookmark_counter")
    private int bookmarkCounter;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<SubjectBean> data;

    /* public int status;
    public String message;
    public Metadata _metadata;
    public List<Datum> data;
    public int bookmark_counter;
    public String method;*/

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

    public int getBookmarkCounter() {
        return bookmarkCounter;
    }

    public void setBookmarkCounter(int bookmarkCounter) {
        this.bookmarkCounter = bookmarkCounter;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<SubjectBean> getData() {
        return data;
    }

    public void setData(List<SubjectBean> data) {
        this.data = data;
    }

}
