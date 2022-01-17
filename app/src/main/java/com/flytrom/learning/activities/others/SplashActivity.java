package com.flytrom.learning.activities.others;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.LoginActivity;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.databinding.ActivitySplashBinding;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    private Handler mHandler;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        mHandler = new Handler();
        mHandler.postDelayed(mSplashRunnable, Constants.SPLASH_TIME);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mSplashRunnable);
        super.onDestroy();
    }

    @Override
    protected int getContentView() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        mHandler.removeCallbacks(mSplashRunnable);
        finish();
    }

    private Runnable mSplashRunnable = () -> {
        if (PrefUtils.getInstance().getUser() != null) {
            goToHomeScreen();
        } else {
            startActivity(new Intent(SplashActivity.this, LoginNewActivity.class));
            goNextAnimation();
            finish();
        }
    };
}