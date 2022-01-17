package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

public class MetadataBean {

    /**
     * page : 1
     * limit : 10
     * remaining_pages : 0
     */

    @SerializedName("page")
    private int page;
    @SerializedName("limit")
    private int limit;
    @SerializedName("remaining_pages")
    private int remainingPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getRemainingPages() {
        return remainingPages;
    }

    public void setRemainingPages(int remainingPages) {
        this.remainingPages = remainingPages;
    }
}
