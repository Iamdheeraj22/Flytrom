package com.flytrom.learning.beans.response_beans.bookmark;

import com.flytrom.learning.beans.response_beans.others.MetadataBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBookmarkedQuesBean {

    /**
     * status : 200
     * message : All Book Marked Questions
     * _metadata : {"page":1,"limit":10,"remaining_pages":0}
     * data : [{"id":"1","user_id":"5","question_id":"1","tag_id":"NA1-0001","question_category":"ATPL - Air Navigation","question_category_chapter":"The Earth, Latitude and Longitude","question":"The approximate compression of the Earth is","level":"Easy","answer_explanation":"The shape of the Earth is a sphere. This shape developed when the Earth formed from a gas-cloud as the spin\nof the cloud caused higher centrifugal forces at the equatorial region than in regions nearer\nthe poles. The flattening is called\ncompression and in the case of the Earth is approximately\n0.3% (1/300th). The Earth\u2019s polar diameter is or 23 nautical miles or 43 km\nless than its equatorial diameter.","question_file":"","answer_file":"https://photos.app.goo.gl/HMKxhDTmpLfrCp4Z7","for_day":null,"created_at":"2020-02-08 04:38:54","updated_at":null,"status":"Active","options":[{"id":"1","question_category":"ATPL - Air Navigation","question_id":"1","name":"3%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"2","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.03%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"3","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.3%","is_answer":"Yes","created_at":"2020-02-08 04:38:55"},{"id":"4","question_category":"ATPL - Air Navigation","question_id":"1","name":" 1/3000","is_answer":"No","created_at":"2020-02-08 04:38:55"}]}]
     * method : bookmarked_questions
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<QuestionBean> data;
    @SerializedName("_metadata")
    private MetadataBean _metadata;

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

    public List<QuestionBean> getData() {
        return data;
    }

    public void setData(List<QuestionBean> data) {
        this.data = data;
    }
}
