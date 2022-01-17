package com.flytrom.learning.model.CommentModel;

import java.util.List;

public class GerCommentModel {
    public int status;
    public String message;
    public List<GetCommentDatum> data;
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

    public List<GetCommentDatum> getData() {
        return data;
    }

    public void setData(List<GetCommentDatum> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
