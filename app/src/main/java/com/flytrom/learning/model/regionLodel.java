package com.flytrom.learning.model;

import java.util.List;

public class regionLodel {

    private int status;
    private String message;
    public List<DataRegion> data;
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

    public List<DataRegion> getData() {
        return data;
    }

    public void setData(List<DataRegion> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
