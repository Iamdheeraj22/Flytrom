package com.flytrom.learning.model;

import com.google.android.exoplayer2.metadata.Metadata;

import java.util.List;

public class FaqModle {

    public int status;
    public String message;
    public Metadata _metadata;
    public List<DatumFaq> data;
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

    public Metadata get_metadata() {
        return _metadata;
    }

    public void set_metadata(Metadata _metadata) {
        this._metadata = _metadata;
    }

    public List<DatumFaq> getData() {
        return data;
    }

    public void setData(List<DatumFaq> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
