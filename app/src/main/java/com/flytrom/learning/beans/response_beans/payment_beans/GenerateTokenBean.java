package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

public class GenerateTokenBean {

    /**
     * status : 200
     * message : Token Generated
     * data : {"status":"OK","message":"Token generated","cftoken":"hA9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.x49JiM5kjNihTNyUTO1UWNiojI0xWYz9lIsgDOwQDM1UDO1EjOiAHelJCLiIlTJJiOik3YuVmcyV3QyVGZy9mIsICMwUjI6ICduV3btFkclRmcvJCLiEDMwIXZkJ3TiojIklkclRmcvJye.v29h5tWfkK4fgZ8iFyrQ28GMGX9p_bq5M6ZL1OiU5WDgHYFD2FPt_0a8eG3Cb6LOlp"}
     * method : generate_token
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private TokenBean data;
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

    public TokenBean getData() {
        return data;
    }

    public void setData(TokenBean data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
