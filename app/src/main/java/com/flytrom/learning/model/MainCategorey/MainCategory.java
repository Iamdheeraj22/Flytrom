package com.flytrom.learning.model.MainCategorey;

import java.util.List;

public class MainCategory {

    public int status;
    public String message;
    public List<DataCategory> data;
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

    public List<DataCategory> getData() {
        return data;
    }

    public void setData(List<DataCategory> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
