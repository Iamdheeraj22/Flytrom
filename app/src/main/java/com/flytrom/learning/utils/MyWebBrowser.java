package com.flytrom.learning.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.flytrom.learning.interfaces.HideLoading;

public class MyWebBrowser extends WebViewClient {

    private HideLoading mHideLoading;

    public MyWebBrowser(HideLoading hideLoading) {
        this.mHideLoading = hideLoading;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (mHideLoading != null) mHideLoading.hideProgress();
    }
}
