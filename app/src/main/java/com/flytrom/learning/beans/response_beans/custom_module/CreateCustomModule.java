package com.flytrom.learning.beans.response_beans.custom_module;

import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateCustomModule {


    /**
     * status : 200
     * message : All Questions
     * data : [{"id":"8","tag_id":"NA1-0008","question_category":"ATPL - Air Navigation ","question_category_chapter":"The Earth, Latitude and Longitude","question":"The angle between the plane of the ecliptic and the plane of equator is\napproximately:","level":"Medium","answer_explanation":"The ecliptic is the great circle cut in the celestial sphere by an extension of the plane of Earth's orbit; equivalently, the apparent annual path of the Sun against the background of the stars. Because of Earth's axial tilt, the ecliptic is inclined at about 23.5° to the celestial equator, an angle known as the obliquity of the ecliptic. The ecliptic intersects the celestial equator at the equinoxes. The ecliptic poles are the two points on the celestial sphere that lie 90° north and south of the plane of the ecliptic. The orbits of the planets, with the exception of Pluto, lie close to the ecliptic.","question_file":"","answer_file":"https://photos.app.goo.gl/H9Qs44rcnhhomJtQ6","for_day":null,"created_at":"2020-02-08 04:39:00","updated_at":null,"status":"Active","options":[{"id":"29","question_category":"ATPL - Air Navigation ","question_id":"8","name":"27.5 degree","is_answer":"No","created_at":"2020-02-08 04:39:00"},{"id":"30","question_category":"ATPL - Air Navigation ","question_id":"8","name":" 25.3 degree","is_answer":"No","created_at":"2020-02-08 04:39:00"},{"id":"31","question_category":"ATPL - Air Navigation ","question_id":"8","name":" 23.5 degree","is_answer":"Yes","created_at":"2020-02-08 04:39:00"},{"id":"32","question_category":"ATPL - Air Navigation ","question_id":"8","name":" 66.5 degree","is_answer":"No","created_at":"2020-02-08 04:39:00"}]},{"id":"1","tag_id":"NA1-0001","question_category":"ATPL - Air Navigation","question_category_chapter":"The Earth, Latitude and Longitude","question":"The approximate compression of the Earth is","level":"Easy","answer_explanation":"The shape of the Earth is a sphere. This shape developed when the Earth formed from a gas-cloud as the spin\nof the cloud caused higher centrifugal forces at the equatorial region than in regions nearer\nthe poles. The flattening is called\ncompression and in the case of the Earth is approximately\n0.3% (1/300th). The Earth\u2019s polar diameter is or 23 nautical miles or 43 km\nless than its equatorial diameter.","question_file":"","answer_file":"https://photos.app.goo.gl/HMKxhDTmpLfrCp4Z7","for_day":null,"created_at":"2020-02-08 04:38:54","updated_at":null,"status":"Active","options":[{"id":"1","question_category":"ATPL - Air Navigation","question_id":"1","name":"3%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"2","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.03%","is_answer":"No","created_at":"2020-02-08 04:38:54"},{"id":"3","question_category":"ATPL - Air Navigation","question_id":"1","name":" 0.3%","is_answer":"Yes","created_at":"2020-02-08 04:38:55"},{"id":"4","question_category":"ATPL - Air Navigation","question_id":"1","name":" 1/3000","is_answer":"No","created_at":"2020-02-08 04:38:55"}]}]
     * method : custom_module
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
