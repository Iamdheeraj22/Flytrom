package com.flytrom.learning.beans.response_beans.custom_module;

import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetCustomModuleQuestionsBean implements Serializable {

    /**
     * status : 200
     * message : All Test Questions
     * data : [{"id":"11","test_id":"1","tag_id":"NA1-0011","question_category":"ATPL - Air Navigation","question":"A great circle on the Earth running from the North Pole to the South Pole is\ncalled:","answer_explanation":"A Great Circle is any circle that circumnavigates the Earth and passes through the center of the Earth. A great circle always divides the Earth in half, thus the Equator is a great circle (but no other latitudes) and all lines of longitude (a meridian) are great circles. The shortest distance between any two points on the Earth lies along a great circle.","question_file":"","answer_file":"","for_day":null,"created_at":"2020-03-07 21:56:07","updated_at":null,"status":"Active","options":[{"id":"37","question_category":"ATPL - Air Navigation","question_id":"11","name":"a longitude","is_answer":"No","created_at":"2020-03-07 21:56:08","has_answered":"0"},{"id":"38","question_category":"ATPL - Air Navigation","question_id":"11","name":" a parallel of latitude","is_answer":"No","created_at":"2020-03-07 21:56:08","has_answered":"0"},{"id":"39","question_category":"ATPL - Air Navigation","question_id":"11","name":" a difference of longitude","is_answer":"No","created_at":"2020-03-07 21:56:08","has_answered":"0"},{"id":"40","question_category":"ATPL - Air Navigation","question_id":"11","name":" a meridian","is_answer":"No","created_at":"2020-03-07 21:56:08","has_answered":"0"}]}]
     * method : test_questions
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<QuestionBean> data;

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
