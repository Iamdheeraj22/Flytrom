package com.flytrom.learning.beans.response_beans.videos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostImageBean {

    /**
     * title : sample: tears-of-steel_teaser
     * description : null
     * duration : 40
     * aspectRatio : 2.4
     * hostnames : ["d1z78r8i505acl.cloudfront.net"]
     * embedUrl : https://d1z78r8i505acl.cloudfront.net/playerAssets/embed/index.html
     * tech : ["dash","hss","hlse","zen"]
     * dash : {"manifest":"https://d1z78r8i505acl.cloudfront.net/media/icAUn7eRb4vHe/82a07bcf/stream.mpd","licenseServers":{"com.widevine.alpha":"https://license.vdocipher.com/auth/wv/:authToken"}}
     * hss : {"bitrates":[796]}
     * posters : [{"url":"https://d1z78r8i505acl.cloudfront.net/poster/5e2ed70e2f6f8.720.jpeg","height":720},{"url":"https://d1z78r8i505acl.cloudfront.net/poster/5e2ed70e2f6f8.240.jpeg","height":240},{"url":"https://d1z78r8i505acl.cloudfront.net/poster/5e2ed70e2f6f8.480.jpeg","height":480}]
     * captions : []
     */

    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private Object description;
    @SerializedName("duration")
    private int duration;
    @SerializedName("aspectRatio")
    private double aspectRatio;
    @SerializedName("embedUrl")
    private String embedUrl;
    @SerializedName("dash")
    private DashBean dash;
    @SerializedName("hss")
    private HssBean hss;
    @SerializedName("hostnames")
    private List<String> hostnames;
    @SerializedName("tech")
    private List<String> tech;
    @SerializedName("posters")
    private List<PostersBean> posters;
    @SerializedName("captions")
    private List<?> captions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public DashBean getDash() {
        return dash;
    }

    public void setDash(DashBean dash) {
        this.dash = dash;
    }

    public HssBean getHss() {
        return hss;
    }

    public void setHss(HssBean hss) {
        this.hss = hss;
    }

    public List<String> getHostnames() {
        return hostnames;
    }

    public void setHostnames(List<String> hostnames) {
        this.hostnames = hostnames;
    }

    public List<String> getTech() {
        return tech;
    }

    public void setTech(List<String> tech) {
        this.tech = tech;
    }

    public List<PostersBean> getPosters() {
        return posters;
    }

    public void setPosters(List<PostersBean> posters) {
        this.posters = posters;
    }

    public List<?> getCaptions() {
        return captions;
    }

    public void setCaptions(List<?> captions) {
        this.captions = captions;
    }

    public static class DashBean {
        /**
         * manifest : https://d1z78r8i505acl.cloudfront.net/media/icAUn7eRb4vHe/82a07bcf/stream.mpd
         * licenseServers : {"com.widevine.alpha":"https://license.vdocipher.com/auth/wv/:authToken"}
         */

        @SerializedName("manifest")
        private String manifest;
        @SerializedName("licenseServers")
        private LicenseServersBean licenseServers;

        public String getManifest() {
            return manifest;
        }

        public void setManifest(String manifest) {
            this.manifest = manifest;
        }

        public LicenseServersBean getLicenseServers() {
            return licenseServers;
        }

        public void setLicenseServers(LicenseServersBean licenseServers) {
            this.licenseServers = licenseServers;
        }

        public static class LicenseServersBean {
            @SerializedName("com.widevine.alpha")
            private String _$ComWidevineAlpha15; // FIXME check this code

            public String get_$ComWidevineAlpha15() {
                return _$ComWidevineAlpha15;
            }

            public void set_$ComWidevineAlpha15(String _$ComWidevineAlpha15) {
                this._$ComWidevineAlpha15 = _$ComWidevineAlpha15;
            }
        }
    }

    public static class HssBean {
        @SerializedName("bitrates")
        private List<Integer> bitrates;

        public List<Integer> getBitrates() {
            return bitrates;
        }

        public void setBitrates(List<Integer> bitrates) {
            this.bitrates = bitrates;
        }
    }

    public static class PostersBean {
        /**
         * url : https://d1z78r8i505acl.cloudfront.net/poster/5e2ed70e2f6f8.720.jpeg
         * height : 720
         */

        @SerializedName("url")
        private String url;
        @SerializedName("height")
        private int height;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
