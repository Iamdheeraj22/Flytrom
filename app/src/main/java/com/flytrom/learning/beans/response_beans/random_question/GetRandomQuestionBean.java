package com.flytrom.learning.beans.response_beans.random_question;

import androidx.room.TypeConverters;

import com.flytrom.learning.room_db.converters.AllTypeConverters;
import com.google.gson.annotations.SerializedName;

public class GetRandomQuestionBean {

    /**
     * status : 200
     * message : Random Question
     * data : {"id":"1","question_category":"Radio Telephony","question_category_chapter":"Aeronauticals Stations","question":"What does FAA stand for?","level":"Easy","answer_explanation":null,"question_file":null,"answer_file":null,"for_day":"2020-02-03","created_at":"2020-02-02 16:22:18","updated_at":null,"status":"Active","has_answered":"0","options":[{"id":"1","question_category":"Radio Telephony","question_id":"1","name":"Federal Avionics Administration","is_answer":"Yes","created_at":"2020-02-02 16:22:19"},{"id":"2","question_category":"Radio Telephony","question_id":"1","name":" Federal Aviation Administration","is_answer":"No","created_at":"2020-02-02 16:22:19"},{"id":"3","question_category":"Radio Telephony","question_id":"1","name":"Fedex Aircraft Administration","is_answer":"No","created_at":"2020-02-02 16:22:19"}]}
     * method : random_question
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private RandomQuestionBean data;
    @SerializedName("method")
    private String method;
    @SerializedName("facts")
    @TypeConverters(value = {AllTypeConverters.class})
    private FactsBean factsBean;

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

    public RandomQuestionBean getData() {
        return data;
    }

    public void setData(RandomQuestionBean data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public FactsBean getFactsBean() {
        return factsBean;
    }

    public void setFactsBean(FactsBean factsBean) {
        this.factsBean = factsBean;
    }
}
