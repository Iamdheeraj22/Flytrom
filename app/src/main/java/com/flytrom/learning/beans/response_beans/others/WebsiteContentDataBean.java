package com.flytrom.learning.beans.response_beans.others;

import com.google.gson.annotations.SerializedName;

public class WebsiteContentDataBean {

    /**
     * id : 1
     * privacy_policy :
     * terms_conditions :
     * about_us : <p open="" sans",="" arial,="" helvetica,="" sans-serif;"="" style="box-sizing: border-box; margin-right: 0px; margin-bottom: 26px; margin-left: 0px; font-size: 16px; line-height: 26px; padding: 0px; color: rgb(118, 118, 118);"></p><div style="margin: 0px 28.8px 0px 14.4px; padding: 0px; width: 436.8px; text-align: left; float: right; color: rgb(0, 0, 0); font-family: " open="" sans",="" arial,="" sans-serif;="" font-size:="" 14px;="" font-style:="" normal;="" font-variant-ligatures:="" font-variant-caps:="" font-weight:="" 400;="" letter-spacing:="" orphans:="" 2;="" text-indent:="" 0px;="" text-transform:="" none;="" white-space:="" widows:="" word-spacing:="" -webkit-text-stroke-width:="" background-color:="" rgb(255,="" 255,="" 255);="" text-decoration-style:="" initial;="" text-decoration-color:="" initial;"=""></div><p></p><div style="margin: 0px 14.4px 0px 28.8px; padding: 0px; width: 436.8px; text-align: left; float: left; color: rgb(0, 0, 0); font-family: " open="" sans",="" arial,="" sans-serif;="" font-size:="" 14px;="" font-style:="" normal;="" font-variant-ligatures:="" font-variant-caps:="" font-weight:="" 400;="" letter-spacing:="" orphans:="" 2;="" text-indent:="" 0px;="" text-transform:="" none;="" white-space:="" widows:="" word-spacing:="" -webkit-text-stroke-width:="" background-color:="" rgb(255,="" 255,="" 255);="" text-decoration-style:="" initial;="" text-decoration-color:="" initial;"=""><p style="margin: 0px 0px 15px; padding: 0px; text-align: justify;"><br><strong style="margin: 0px; padding: 0px;">Lorem Ipsum</strong><span>&nbsp;</span>is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.</p></div><div style="color: rgb(34, 34, 34); font-size: small; font-family: arial, helvetica, sans-serif;"></div>
     * contact_us : <p style="margin-right: 0px; margin-bottom: 15px; margin-left: 0px; padding: 0px; text-align: justify; font-family: &quot;Open Sans&quot;, Arial, sans-serif;">It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).</p><div><br></div>
     */

    @SerializedName("id")
    private String id;
    @SerializedName("privacy_policy")
    private String privacyPolicy;
    @SerializedName("terms_conditions")
    private String termsConditions;
    @SerializedName("about_us")
    private String aboutUs;
    @SerializedName("contact_us")
    private String contactUs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(String termsConditions) {
        this.termsConditions = termsConditions;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }
}
