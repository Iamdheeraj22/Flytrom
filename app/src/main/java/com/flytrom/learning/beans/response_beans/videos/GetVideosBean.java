package com.flytrom.learning.beans.response_beans.videos;

import com.flytrom.learning.beans.response_beans.others.MetadataBean;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideosBean {

    /**
     * status : 200
     * message : All Videos
     * _metadata : {"page":1,"limit":10,"remaining_pages":0}
     * data : [{"id":"4","videocypherid":"6bd0ba90cb3734e4aa754508a7f7b215","type":"Paid","title":"Fourth Video","category":"ATPL - Air Navigation","bucket":"vdo-ap-southeast","videokey":"orig/5e3e436710935","etag":"\"1f3d13e2110765f99ee8cf9dd124afd0\"","notes":"notes/2020-02/20200228_EP-1612-1582916033.pdf","length":46,"is_downloaded":"No","created_at":"2020-02-08 05:15:17","updated_at":null,"has_rated":"0","average_rating":"3","status":"Unattempted","video_link":"https://player.vdocipher.com/playerAssets/1.x/vdo/embed/index.html#otp=20160313versASE323pv8q2BE7gejjwsrUd7C2O0jj9oDEH4JN0swRUQlJuypdBM&playbackInfo=eyJ2aWRlb0lkIjoiNmJkMGJhOTBjYjM3MzRlNGFhNzU0NTA4YTdmN2IyMTUifQ==","topics":[{"id":"1","video_id":"4","topic":"First TopicBean","duration":"1","created_on":"2020-02-08 05:16:18","status":"Active"},{"id":"2","video_id":"4","topic":"Second TopicBean","duration":"2","created_on":"2020-02-08 05:16:31","status":"Active"}],"slides":[{"id":"4","video_id":"4","file":"video_slides/2020-02/20200208_EP-2426-1581140592.jpg"},{"id":"5","video_id":"4","file":"video_slides/2020-02/20200208_EP-4703-1581140606.png"}],"otp":"20160313versASE323pv8q2BE7gejjwsrUd7C2O0jj9oDEH4JN0swRUQlJuypdBM","playbackInfo":"eyJ2aWRlb0lkIjoiNmJkMGJhOTBjYjM3MzRlNGFhNzU0NTA4YTdmN2IyMTUifQ=="},{"id":"3","videocypherid":"7f7a4b757348c8c9ab8569749e6eac97","type":"Paid","title":"Third Video","category":"ATPL - Air Navigation","bucket":"vdo-ap-southeast","videokey":"orig/5e3e43108e706","etag":"\"b6adfa33f0673c7eff196748b21459c5\"","notes":"notes/2020-02/20200208_EP-4021-1581139067.pdf","length":1352,"is_downloaded":"No","created_at":"2020-02-08 05:14:05","updated_at":null,"has_rated":"0","average_rating":"0","status":"Unattempted","video_link":"https://player.vdocipher.com/playerAssets/1.x/vdo/embed/index.html#otp=20160313versASE323KrzKaO6FyIflDjRiborHHGpDn3A3ktMaF8XBshEyelGUGL&playbackInfo=eyJ2aWRlb0lkIjoiN2Y3YTRiNzU3MzQ4YzhjOWFiODU2OTc0OWU2ZWFjOTcifQ==","topics":[{"id":"3","video_id":"3","topic":"First TopicBean","duration":"1","created_on":"2020-02-08 05:17:25","status":"Active"},{"id":"4","video_id":"3","topic":"Second TopicBean","duration":"2","created_on":"2020-02-08 05:17:38","status":"Active"}],"slides":[{"id":"3","video_id":"3","file":"video_slides/2020-02/20200208_EP-8320-1581139639.jpg"}],"otp":"20160313versASE323KrzKaO6FyIflDjRiborHHGpDn3A3ktMaF8XBshEyelGUGL","playbackInfo":"eyJ2aWRlb0lkIjoiN2Y3YTRiNzU3MzQ4YzhjOWFiODU2OTc0OWU2ZWFjOTcifQ=="}]
     * method : videos
     */

    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;
    @SerializedName("_metadata")
    private MetadataBean metadata;
    @SerializedName("method")
    private String method;
    @SerializedName("data")
    private List<VideoBean> data;

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

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<VideoBean> getData() {
        return data;
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
    }
}
