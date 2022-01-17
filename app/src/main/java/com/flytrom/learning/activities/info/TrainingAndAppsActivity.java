package com.flytrom.learning.activities.info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.others.GetUrlDataBean;
import com.flytrom.learning.beans.response_beans.others.GetUrlsBean;
import com.flytrom.learning.beans.response_beans.others.GetWebsiteContentBean;
import com.flytrom.learning.databinding.ActivityTrainingAndAppsBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.interfaces.HideLoading;
import com.flytrom.learning.model.UrlModle;
import com.flytrom.learning.model.WebContent.WebContentPojo;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.MyWebBrowser;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("SetJavaScriptEnabled")
public class TrainingAndAppsActivity extends BaseActivity<ActivityTrainingAndAppsBinding> implements HideLoading {

    private String mFrom;
    private Call<GetUrlsBean> mCallGetUrls;
    Apis apisInterface;
    private Call<GetWebsiteContentBean> mCallWebsiteContent;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apisInterface = AppController.getInstance().getApis();
        initView();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_training_and_apps;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallGetUrls != null) mCallGetUrls.cancel();
        super.onDestroy();
    }

    private void initView() {
        mFrom = getIntent().getStringExtra("from");
        setBaseCallback(baseCallback);
        if (mFrom != null) {
            binding.toolbar.textTitle.setText(mFrom);
            binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            if (checkIsInternetOn()) {
                switch (mFrom) {
                    case "Terms and Conditions": {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                        layoutParams.setMargins(0, -100, 0, -3400);
                        binding.webView.setLayoutParams(layoutParams);
                        setUrlOnWebView("https://learning.flytrom.com/terms-and-conditions/");
                    }
                    break;
                    case "FAQs": {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                        layoutParams.setMargins(0, -100, 0, -3400);
                        binding.webView.setLayoutParams(layoutParams);
                        setUrlOnWebView("https://learning.flytrom.com/faqs/");
                    }
                    break;
                    case "About Us": {
                        binding.progress.setVisibility(View.VISIBLE);
                        apisInterface.getWebsiteContent2(getHeader()).enqueue(new Callback<WebContentPojo>() {
                            @Override
                            public void onResponse(Call<WebContentPojo> call, Response<WebContentPojo> response) {
                                binding.progress.setVisibility(View.GONE);
                                if (response.isSuccessful()){
                                    WebContentPojo webContentPojo = response.body();
                                    String htmlText = webContentPojo.getData().get(0).getAbout_us();
                                    binding.webView.getSettings().setJavaScriptEnabled(true);
                                    binding.webView.loadData(htmlText, "text/html", "UTF-8");
                                   // binding.textAbout.setText(Html.fromHtml(webContentPojo.getData().get(0).getAbout_us()));
                                }else {
                                    Toast.makeText(TrainingAndAppsActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<WebContentPojo> call, Throwable t) {
                                binding.progress.setVisibility(View.GONE);
                                Toast.makeText(TrainingAndAppsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    break;
                    case "Contact Us":{
                        binding.progress.setVisibility(View.VISIBLE);
                        apisInterface.getWebsiteContent2(getHeader()).enqueue(new Callback<WebContentPojo>() {
                            @Override
                            public void onResponse(Call<WebContentPojo> call, Response<WebContentPojo> response) {
                                binding.progress.setVisibility(View.GONE);
                                if (response.isSuccessful()){
                                    WebContentPojo webContentPojo = response.body();
                                    String htmlText = webContentPojo.getData().get(0).getContact_us();
                                    binding.webView.getSettings().setJavaScriptEnabled(true);
                                    binding.webView.loadData(htmlText, "text/html", "UTF-8");
                                   // binding.textAbout.setText(Html.fromHtml(webContentPojo.getData().get(0).getContact_us()));
                                }else {
                                    Toast.makeText(TrainingAndAppsActivity.this, getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<WebContentPojo> call, Throwable t) {
                                binding.progress.setVisibility(View.GONE);
                                Toast.makeText(TrainingAndAppsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


//                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                                RelativeLayout.LayoutParams.MATCH_PARENT);
//                        layoutParams.setMargins(0, -100, 0, -3400);
//                        binding.webView.setLayoutParams(layoutParams);
//                        setUrlOnWebView("https://flytrom.com/");
                    }
                        getWebsiteContent();
                        break;
                    default:
                        getUrls();
                }
            } else {
                binding.webView.setVisibility(View.GONE);
            }
        }
    }

    private void getUrls() {
        showAvlIndicator();
        mCallGetUrls = AppController.getInstance().getApis().getUrls(getHeader());
        mCallGetUrls.enqueue(new ResponseHandler<GetUrlsBean>() {
            @Override
            public void onSuccess(GetUrlsBean getUrlsBean) {
                if (getUrlsBean != null) {
                    if (getUrlsBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (mFrom.equals(Constants.SIDE_MENU_TITLES[2]))
                            setUrlOnWebView(getUrlsBean.getData().get(1).getUrl());
                        else
                            setUrlOnWebView(getUrlsBean.getData().get(0).getUrl());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetUrlsBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void setUrlOnWebView(String url) {
        binding.webView.setWebViewClient(new MyWebBrowser(this));
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.loadUrl(url);
    }

    private void loadHtmlOnWebView(String html) {
        binding.webView.setWebViewClient(new MyWebBrowser(this));
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.loadData(html, "text/html", "UTF-8");
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            onBackPressed();
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.binding.webView.canGoBack()) {
            this.binding.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void hideProgress() {
        hideAvlIndicator();
    }


    private void showAvlIndicator() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.avl.show();
    }

    private void hideAvlIndicator() {
        binding.progress.setVisibility(View.GONE);
        binding.avl.hide();
    }

    private void getWebsiteContent() {
        showAvlIndicator();
        mCallWebsiteContent = AppController.getInstance().getApis().getWebsiteContent(getHeader());
        mCallWebsiteContent.enqueue(new ResponseHandler<GetWebsiteContentBean>() {
            @Override
            public void onSuccess(GetWebsiteContentBean responseBean) {
                if (responseBean != null) {
                    if (responseBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (mFrom.equals(getString(R.string.text_about_us)))
                            loadHtmlOnWebView(responseBean.getData().getAboutUs());
                        else
                            loadHtmlOnWebView(responseBean.getData().getContactUs());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetWebsiteContentBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
