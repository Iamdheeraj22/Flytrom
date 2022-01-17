package com.flytrom.learning.beans.response_beans.q_bank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetQuestionBankChapterBean {


    /**
     * status : 200
     * message : All Question Chapters
     * _metadata : {"page":1,"limit":10,"remaining_pages":0}
     * data : [{"id":"1","type":"Paid","question_category":"ATPL - Air Navigation","title":"The Earth, Latitude and Longitude","created_at":"2020-02-08 04:38:54","updated_at":null,"status":"Unattempted","has_rated":"1","average_rating":"4","total_mcqs":"11"}]
     * method : question_bank_chapters
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("_metadata")
    private MetadataBean _metadata;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<ChapterBean> data;

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

    public MetadataBean get_metadata() {
        return _metadata;
    }

    public void set_metadata(MetadataBean _metadata) {
        this._metadata = _metadata;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ChapterBean> getData() {
        return data;
    }

    public void setData(List<ChapterBean> data) {
        this.data = data;
    }

    public static class MetadataBean {
        /**
         * page : 1
         * limit : 10
         * remaining_pages : 0
         */

        private int page;
        private int limit;
        private int remaining_pages;

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

        public int getRemaining_pages() {
            return remaining_pages;
        }

        public void setRemaining_pages(int remaining_pages) {
            this.remaining_pages = remaining_pages;
        }
    }
}
