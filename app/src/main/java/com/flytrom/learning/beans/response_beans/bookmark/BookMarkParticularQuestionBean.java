package com.flytrom.learning.beans.response_beans.bookmark;

import com.google.gson.annotations.SerializedName;

public class BookMarkParticularQuestionBean {
    /**
     * status : 200
     * message : Question Unbookmarked Successfully
     * data : 0
     * method : bookmark_question
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private int data;
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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
