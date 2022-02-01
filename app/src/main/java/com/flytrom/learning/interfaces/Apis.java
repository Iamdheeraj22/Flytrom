package com.flytrom.learning.interfaces;

import androidx.annotation.Nullable;

import com.flytrom.learning.beans.response_beans.bookmark.BookMarkParticularQuestionBean;
import com.flytrom.learning.beans.response_beans.others.GetUrlsBean;
import com.flytrom.learning.beans.response_beans.auth.SignUpBean;
import com.flytrom.learning.beans.response_beans.others.GetWebsiteContentBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CheckCouponBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GenerateTokenBean;
import com.flytrom.learning.beans.response_beans.bookmark.GetBookmarkedQuesBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GetNewOrderIdBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GetPlansBean;
import com.flytrom.learning.beans.response_beans.q_bank.GetQuestionBankChapterBean;
import com.flytrom.learning.beans.response_beans.random_question.GetRandomQuestionBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.videos.GetVideosBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.test_bean.GetTestsBean;
import com.flytrom.learning.beans.response_beans.videos.PostImageBean;
import com.flytrom.learning.model.ChangeEmailPhoneModel;
import com.flytrom.learning.model.CommentModel.GerCommentModel;
import com.flytrom.learning.model.CouponModel.CouponPojo;
import com.flytrom.learning.model.EmailOtp;
import com.flytrom.learning.model.FaqModle;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.model.MainCategorey.MainCategory;
import com.flytrom.learning.model.CommentModel.SendCommentModel;
import com.flytrom.learning.model.SendOtpPojo;
import com.flytrom.learning.model.UrlModle;
import com.flytrom.learning.model.WebContent.WebContentPojo;
import com.flytrom.learning.model.progressModel.ProgressModel;
import com.flytrom.learning.model.regionLodel;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by androiddev on 8/24/2018.
 */
public interface Apis {

    @FormUrlEncoded
    @POST("signup")
    Call<LoginModel> signUp(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<SignUpBean> forgotPassword(@Field("email") String email);

    @GET("checkEmailAvailability")
    Call<SignUpBean> checkEmailAvailability(@Query("email") String email);

    @Multipart
    @POST("update_profile_picture")
    Call<LoginModel> updateProfilePicture(@HeaderMap Map<String, String> header,
                                          @Nullable @Part MultipartBody.Part part);

    @PUT("sign_out")
    Call<LoginModel> logOut(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @PUT("change_password")
    Call<LoginModel> changePassword(@HeaderMap Map<String, String> headerMap,
                                    @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_profile")
    Call<LoginModel> updateProfile(@HeaderMap Map<String, String> headerMap,
                                   @FieldMap Map<String, String> map);

    @GET("get_profile")
    Call<LoginModel> getProfile(@HeaderMap Map<String, String> headerMap);

    @GET("get_progress")
    Call<ProgressModel> getProgress(@HeaderMap Map<String, String> headerMap);

    @GET("question_bank_categories/{currentPage}/50")
    Call<GetSubjectsBean> getSubjects(@HeaderMap Map<String, String> headerMap,
                                      @Path("currentPage") String currentPage,
                                      @Query("order") String order,
                                      @Query("type") String type);

    @GET("question_bank_categories/{currentPage}/50")
    Call<GetSubjectsBean> getSubjects2(@HeaderMap Map<String, String> headerMap,
                                       @Path("currentPage") String currentPage,
                                       @Query("order") String order,
                                       @Query("type") String type,
                                       @Query("category_id") String category_id);

    @GET("videos/{currentPage}/20")
    Call<GetVideosBean> getVideos(@HeaderMap Map<String, String> headerMap,
                                  @Path("currentPage") String currentPage,
                                  @Query("chapter") String chapterName,
                                  @Query("free") String free);

    @GET("bookmarked_questions/{currentPage}/20")
    Call<GetBookmarkedQuesBean> getBookmarkedQuestions(@HeaderMap Map<String, String> headerMap,
                                                       @Path("currentPage") String currentPage);

    @FormUrlEncoded
    @POST("update_video_status")
    Call<SuccessBean> updateVideoStatus(@HeaderMap Map<String, String> headerMap,
                                        @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("mark_complete")
    Call<SuccessBean> markCompleted(@HeaderMap Map<String, String> headerMap,
                                    @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("mark_downloaded")
    Call<SuccessBean> markDownload(@HeaderMap Map<String, String> headerMap,
                                   @FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("bookmark_question")
    Call<BookMarkParticularQuestionBean> bookmarkQuestion(@HeaderMap Map<String, String> headerMap,
                                                          @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rate_video")
    Call<SuccessBean> rateVideo(@HeaderMap Map<String, String> headerMap,
                                @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rate_chapter")
    Call<SuccessBean> rateChapter(@HeaderMap Map<String, String> headerMap,
                                  @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("rate_test")
    Call<SuccessBean> rateTest(@HeaderMap Map<String, String> headerMap,
                               @FieldMap Map<String, String> map);

    @GET("random_question")
    Call<GetRandomQuestionBean> getRandomMCQ(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST("create_custom_module")
    Call<GetCustomModuleBean> createCustomModule(@HeaderMap Map<String, String> headerMap,
                                                 @FieldMap Map<String, String> map);

    @GET("custom_module")
    Call<GetCustomModuleQuestionsBean> getCustomModuleQuestions(@HeaderMap Map<String, String> headerMap,
                                                                @Query("custom_module_id") String moduleId);

    @DELETE("delete_custom_module")
    Call<SuccessBean> deleteCustomModule(@HeaderMap Map<String, String> headerMap);

    @GET("get_custom_module")
    Call<GetCustomModuleBean> getCustomModule(@HeaderMap Map<String, String> headerMap);

    @GET("question_bank_chapters/{currentPage}/20")
    Call<GetQuestionBankChapterBean> getChapters(@HeaderMap Map<String, String> headerMap,
                                                 @Path("currentPage") String currentPage,
                                                 @Query("question_category") String questionCategory,
                                                 @Query("type") String type);

    @GET("chapter_questions/{currentPage}/1000")
    Call<GetCustomModuleQuestionsBean> getChapterQuestions(@HeaderMap Map<String, String> headerMap,
                                                           @Path("currentPage") String currentPage,
                                                           @Query("question_category_chapter") String chapterName,
                                                           @Query("type") String type);

    @FormUrlEncoded
    @POST("solve_mcq")
    Call<SuccessBean> solveMCQApi(@HeaderMap Map<String, String> headerMap,
                                  @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("solve_test")
    Call<SuccessBean> solveTest(@HeaderMap Map<String, String> headerMap,
                                @FieldMap Map<String, String> map);

    @GET("tests/{currentPage}/20")
    Call<GetTestsBean> getTests(@HeaderMap Map<String, String> headerMap,
                                @Path("currentPage") String currentPage,
                                @Query("subject") String subject,
                                @Query("type") String type);

    @GET("tests/{currentPage}/20")
    Call<GetTestsBean> getFreeTests(@HeaderMap Map<String, String> headerMap,
                                @Path("currentPage") String currentPage,
                                @Query("type") String type);

    @GET("test_questions/{currentPage}/1000")
    Call<GetCustomModuleQuestionsBean> getTestQuestions(@HeaderMap Map<String, String> headerMap,
                                                        @Path("currentPage") String currentPage,
                                                        @Query("test_id") int chapterName);

    @FormUrlEncoded
    @POST("report_content")
    Call<SuccessBean> reportContent(@HeaderMap Map<String, String> headerMap,
                                    @FieldMap Map<String, String> map);

    @GET("check_coupon")
    Call<CheckCouponBean> checkCoupon(@HeaderMap Map<String, String> headerMap,
                                      @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("reset_test")
    Call<SuccessBean> resetTest(@HeaderMap Map<String, String> headerMap,
                                @Field("test_id") int testId);


    @FormUrlEncoded
    @POST("delete_comment")
    Call<SuccessBean> deleteComment(@HeaderMap Map<String, String> headerMap,
                                    @Field("comment_id") String comment_id);

    @FormUrlEncoded
    @POST("edit_comment")
    Call<SuccessBean> editComment(@HeaderMap Map<String, String> headerMap,
                                  @Field("comment_id") String comment_id,
                                  @Field("comment") String comment);

    @FormUrlEncoded
    @POST("reset_qb")
    Call<SuccessBean> resetQbankChapter(@HeaderMap Map<String, String> headerMap,
                                        @Field("chapter_id") int testId);

    @FormUrlEncoded
    @POST("answer_random_question")
    Call<SuccessBean> answerRandomQuestion(@HeaderMap Map<String, String> headerMap,
                                           @FieldMap Map<String, String> map);

    @Multipart
    @POST("report_piracy")
    Call<SuccessBean> reportPiracy(@HeaderMap Map<String, String> header,
                                   @Nullable @PartMap HashMap<String, RequestBody> partMap,
                                   @Part MultipartBody.Part part);

    @GET("get_plans/{currentPage}/100")
    Call<GetPlansBean> getPlans(@HeaderMap Map<String, String> headerMap,
                                @Path("currentPage") String currentPage);

    @GET("get_plan_coupons/{currentPage}/100")
    Call<CouponPojo> getPlansCoupon(@HeaderMap Map<String, String> headerMap,
                                    @Path("currentPage") String currentPage);

    @GET("new_order_id")
    Call<GetNewOrderIdBean> getNewOrderId(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST("generate_token")
    Call<GenerateTokenBean> generateToken(@HeaderMap Map<String, String> headerMap,
                                          @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("make_payment")
    Call<SuccessBean> makePaymentApi(@HeaderMap Map<String, String> headerMap,
                                     @FieldMap Map<String, String> map);

    @GET("get_urls")
    Call<GetUrlsBean> getUrls(@HeaderMap Map<String, String> headerMap);

    @GET("get_urls")
    Call<UrlModle> getUrls2(@HeaderMap Map<String, String> headerMap);

    @GET("website_content")
    Call<GetWebsiteContentBean> getWebsiteContent(@HeaderMap Map<String, String> headerMap);

    @GET("website_content")
    Call<WebContentPojo> getWebsiteContent2(@HeaderMap Map<String, String> headerMap);

    @FormUrlEncoded
    @POST("reset_content")
    Call<SuccessBean> resetContent(@HeaderMap Map<String, String> headerMap,
                                   @Field("type") String type);

    @FormUrlEncoded
    @POST("loginWithPhone")
    Call<LoginModel> loginWithPhone(@Field("phone_number") String phone_number,
                                    @Field("country_code") String country_code,
                                    @Field("country") String country,
                                    @Field("device_type") String device_type,
                                    @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("changePhone")
    Call<ChangeEmailPhoneModel> changePhone(@HeaderMap Map<String, String> headerMap,
                                            @Field("phone_number") String phone_number,
                                            @Field("country_code") String country_code,
                                            @Field("country") String country);

    @FormUrlEncoded
    @POST("loginWithEmail")
    Call<LoginModel> loginWithEmail(@Field("email") String email,
                                    @Field("device_type") String device_type,
                                    @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("changeEmail")
    Call<ChangeEmailPhoneModel> changeEmail(@HeaderMap Map<String, String> headerMap,
                                            @Field("email") String email);

    @FormUrlEncoded
    @POST("emailVerificationCode")
    Call<EmailOtp> emailVerificationCode(@Field("email") String email);

    @FormUrlEncoded
    @POST("update_region")
    Call<LoginModel> updateRegion(@HeaderMap Map<String, String> headerMap,
                                  @Field("region_id") String region_id);

    @GET("meta/{video_id}")
    Call<PostImageBean> getPosterImage(@Path("video_id") String currentPage);

    @GET("otp")
    Call<SendOtpPojo> sendOtp(@Query("template_id") String template_id,
                              @Query("mobile") String mobile,
                              @Query("otp_length") String otp_length,
                              @Query("authkey") String authkey,
                              @Query("sender") String sender);

    @GET("otp/verify")
    Call<SendOtpPojo> verifyOtp(@Query("authkey") String authkey,
                                @Query("mobile") String mobile,
                                @Query("otp") String otp);

    @GET("otp/retry")
    Call<SendOtpPojo> retryOtp(@Query("authkey") String authkey,
                               @Query("retrytype") String retrytype,
                               @Query("mobile") String mobile);

    @GET("regions")
    Call<regionLodel> getRegions();

    @GET("main_categories/{currentPage}/100")
    Call<MainCategory> main_categories(@HeaderMap Map<String, String> headerMap,
                                       @Path("currentPage") String currentPage,
                                       @Query("order") String order,
                                       @Query("type") String type);

    @GET("faqs/{currentPage}/100")
    Call<FaqModle> getFaq(@HeaderMap Map<String, String> headerMap,
                          @Path("currentPage") String currentPage,
                          @Query("type") String type);

    @GET("video_comments/{currentPage}/100")
    Call<GerCommentModel> getVideoComment(@HeaderMap Map<String, String> headerMap,
                                          @Path("currentPage") String currentPage,
                                          @Query("video_id") String video_id);

    @FormUrlEncoded
    @POST("post_comment")
    Call<SendCommentModel> sendComment(@HeaderMap Map<String, String> headerMap,
                                       @FieldMap Map<String, String> map);


}
