package com.flytrom.learning.model.LoginModel;

import com.flytrom.learning.beans.response_beans.auth.UserDataBean;

public class LoginModel {
    private int status;
    private String message;
    private UserDataBean data;
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

    public UserDataBean getData() {
        return data;
    }

    public void setData(UserDataBean data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
