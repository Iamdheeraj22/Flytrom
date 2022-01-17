package com.flytrom.learning.activities.test_menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.databinding.ActivityRateContentBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;

public class RateContentActivity extends BaseActivity<ActivityRateContentBinding> {

    int pStatus = 0;
    private Handler handler = new Handler();
    private String mQuestionChapterId, mChapterName, mTestId, mModuleId, fromScreen;
    private int mCorrectAnswer = 0;
    private int mTotalQuestion = 0;
    private Call<SuccessBean> mCallRate;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHomeScreen();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_rate_content;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallRate != null) mCallRate.cancel();
        super.onDestroy();
    }

    private void initView() {

        setBaseCallback(baseCallback);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_normal_long_run);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(() -> {
                    getData();
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.imageTickMark.startAnimation(animation);

    }

    private void getData() {


        binding.linearCompleted.setVisibility(View.INVISIBLE);
        binding.relativeRate.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromScreen = bundle.getString("fromScreen");
            mQuestionChapterId = bundle.getString("question_chapter_id");
            mChapterName = bundle.getString("chapter_name");
            mTestId = bundle.getString("test_id");
            mModuleId = bundle.getString("module_id");
            mTotalQuestion = bundle.getInt("totalQuestions");
            mCorrectAnswer = bundle.getInt("correctCount");

            setData();
        }
    }

    private void setData() {
        if (fromScreen.equals("CustomModule")) {
            binding.textYourRating.setVisibility(View.GONE);
            binding.ratingBar.setVisibility(View.GONE);
            binding.textSubmitRating.setVisibility(View.GONE);
        }

        binding.textYouHaveAnswer.setText(String.format("You have nailed %d of %d questions", mCorrectAnswer, mTotalQuestion));
        int progress = (mCorrectAnswer * 100) / mTotalQuestion;
        setRateContentData(progress);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_close:
                goToHomeScreen();
                break;
            case R.id.button_review:
                if (AppController.getInstance().isInternetOn()) goToNextScreen();
                else showToast(getString(R.string.no_internet));
                break;
            case R.id.text_submit_rating:
                if (binding.ratingBar.getRating() > 0) {
                    rateApi();
                } else
                    showErrorToast("Please give rating");
                break;
        }
    };

    private void goToNextScreen() {
        Bundle bundle = new Bundle();
        bundle.putString("from", fromScreen);
        if (mQuestionChapterId != null)
            bundle.putString("question_chapter_id", mQuestionChapterId);
        if (mChapterName != null)
            bundle.putString("chapter_name", mChapterName);
        if (mTestId != null)
            bundle.putString("test_id", mTestId);
        if (mModuleId != null)
            bundle.putString("module_id", mModuleId);

        startActivity(new Intent(RateContentActivity.this, ReviewTestActivity.class).putExtras(bundle));
        goNextAnimation();
    }


    @SuppressLint("SetTextI18n")
    private void setRateContentData(int rating) {

        new Thread(() -> {
            // TODO Auto-generated method stub
            while (pStatus < rating) {
                pStatus += 1;

                handler.post(() -> {
                    // TODO Auto-generated method stub
                    binding.progressBar.setProgress(pStatus);
                    binding.textPercent.setText(pStatus + "%");

                });
                try {
                    // Sleep for 200 milliseconds.
                    // Just to display the progress slowly
                    Thread.sleep(32); //thread will take approx 3 seconds to finish
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void rateApi() {
        if (mCallRate != null) mCallRate.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("rate", String.valueOf(binding.ratingBar.getRating()));
        if (mQuestionChapterId != null) {
            map.put("question_chapter_id", mQuestionChapterId);
            mCallRate = AppController.getInstance().getApis().rateChapter(getHeader(), map);
        } else {
            if (mTestId != null) {
                map.put("test_id", mTestId);
                mCallRate = AppController.getInstance().getApis().rateTest(getHeader(), map);
            }
        }

        mCallRate.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());
                        //goToHome();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
