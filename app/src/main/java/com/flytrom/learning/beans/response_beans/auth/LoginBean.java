package com.flytrom.learning.beans.response_beans.auth;

import com.flytrom.learning.beans.response_beans.payment_beans.PlanTypesBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginBean {

    /**
     * status : 200
     * message : User Profile
     * data : {"id":"15","session_id":"f9865ee87e45b24cd042ba1e49213e25","first_name":"Test","last_name":"User","email":"testing@yopmail.com","is_email_verified":"Pending","password":"$6$s8^3s;$S3HhiiOkyDXhF.zJkNXM2SsTlFsO5Irhlk.UF0D059QYUitI/RvXj/nOkcZtPiGR6DmLwOFLvraaa2Sfux.iF0","phone_number":"8360748452","is_phone_verified":"Pending","profile_picture":"profile_pictures/2020-02/760f3812fcff9a95f73aec02f84f148f.png","cpl_number":"321","designation":"0","atpl_number":"321","timezone":"","user_type":"User","device_type":"ANDROID","device_token":"eBsKeaMxv6s:APA91bHSMftKMgVHdtLy84hotwBfO1Md4Q03mXDrs37wWt7ITJPk45kztxKQJ2-ZW91QFkfO7tjJeVQwQLt5t2wDqNxr-dstp7MH9oFZHgV-OrV03yFMsAm_4vAa8CuJd497UdAeISS0","created_at":"2020-01-31 13:05:17","updated_at":null,"account_status":"Active","video_completed":"4","question_attempted":"0","purchased_plans":[{"id":"3","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294763","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-04 16:35:39","created_at":"2020-04-04 11:44:02","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null},{"id":"4","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294763","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-04 16:35:39","created_at":"2020-04-04 11:44:14","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null},{"id":"5","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294763","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-04 16:35:39","created_at":"2020-04-04 11:45:00","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null},{"id":"6","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294763","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-04 16:35:39","created_at":"2020-04-04 11:52:03","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null},{"id":"7","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294796","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-04 17:23:58","created_at":"2020-04-04 11:54:10","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null},{"id":"11","user_id":"15","plan_id":"3","plan_metadata_id":"0","referenceId":"294930","paymentMode":"WALLET","txStatus":"SUCCESS","txTime":"2020-04-05 02:10:12","created_at":"2020-04-04 20:40:13","has_expired":"1","plan_details":{"id":"3","plan_name":"Radio Telephony (RTR)","has_meta":"0","amount":"20000","subjects":"Radio Telephony (RTR)","plan_type":"Plan 3","created_at":"2020-03-30 13:50:54","updated_at":"2020-03-30 13:50:54","status":"Active","plan_types":{"id":"4","title":"Plan 3","videos":"1","question_bank":"0","tests":"0"}},"user_coupon_applied":null}]}
     * method : get_profile
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private UserDataBean data;
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
