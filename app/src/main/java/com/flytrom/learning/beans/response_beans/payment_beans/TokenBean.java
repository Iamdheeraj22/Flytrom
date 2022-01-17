package com.flytrom.learning.beans.response_beans.payment_beans;

import com.google.gson.annotations.SerializedName;

public class TokenBean {

    /**
     * status : OK
     * message : Token generated
     * cftoken : hA9JCN4MzUIJiOicGbhJCLiQ1VKJiOiAXe0Jye.x49JiM5kjNihTNyUTO1UWNiojI0xWYz9lIsgDOwQDM1UDO1EjOiAHelJCLiIlTJJiOik3YuVmcyV3QyVGZy9mIsICMwUjI6ICduV3btFkclRmcvJCLiEDMwIXZkJ3TiojIklkclRmcvJye.v29h5tWfkK4fgZ8iFyrQ28GMGX9p_bq5M6ZL1OiU5WDgHYFD2FPt_0a8eG3Cb6LOlp
     */

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("cftoken")
    private String cftoken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCftoken() {
        return cftoken;
    }

    public void setCftoken(String cftoken) {
        this.cftoken = cftoken;
    }
}
