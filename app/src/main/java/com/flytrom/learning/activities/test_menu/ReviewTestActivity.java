package com.flytrom.learning.activities.test_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.q_bank_menu.ParticularReviewQuesActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.normal_beans.SubscribePlansBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivityReviewTestBinding;
import com.flytrom.learning.databinding.ItemReviewTestBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class ReviewTestActivity extends BaseActivity<ActivityReviewTestBinding> {

    private SimpleRecyclerViewAdapter<QuestionBean, ItemReviewTestBinding> mReviewTestAdapter;
    private Call<GetCustomModuleQuestionsBean> mCallGetCustomModuleQuestions;
    private List<SubscribePlansBean> list;
    private String mQuestionChapterId, mChapterName, mTestId, mModuleId, fromScreen;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_review_test;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mCallGetCustomModuleQuestions != null) mCallGetCustomModuleQuestions.cancel();
        super.onDestroy();
    }

    private void initView() {
        binding.toolbar.textTitle.setText(getString(R.string.review));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SubscribePlansBean bean = new SubscribePlansBean("", "", i, false);
            list.add(bean);
        }
        mReviewTestAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_review_test, BR.bean, (v, questionBean) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("questionBean", questionBean);
            startActivity(new Intent(ReviewTestActivity.this, ParticularReviewQuesActivity.class).putExtras(bundle));
            goNextAnimation();
        });
        binding.recyclerQuestions.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerQuestions.setAdapter(mReviewTestAdapter);

        setBaseCallback(baseCallback);
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fromScreen = bundle.getString("from");
            mQuestionChapterId = bundle.getString("question_chapter_id");
            mChapterName = bundle.getString("chapter_name");
            mTestId = bundle.getString("test_id");
            mModuleId = bundle.getString("module_id");

            if (mModuleId != null) {
                getCustomModuleQuestions(mModuleId);
            }

            if (mChapterName != null) {
                getChapterQuestions(mChapterName);
            }
            if (mTestId != null) {
                getTestQuestions(mTestId);
            }

            //getDateFromApi(();
        }
    }

    private void getCustomModuleQuestions(String moduleId) {
        mCallGetCustomModuleQuestions = AppController.getInstance().getApis().getCustomModuleQuestions(getHeader(),
                moduleId);
        getQuestionsApi(mCallGetCustomModuleQuestions);
    }

    private void getChapterQuestions(String chapterName) {
        mCallGetCustomModuleQuestions = AppController.getInstance().getApis()
                .getChapterQuestions(getHeader(), "1", chapterName,"Paid");
        getQuestionsApi(mCallGetCustomModuleQuestions);
    }

    private void getTestQuestions(String mTestId) {
        mCallGetCustomModuleQuestions = AppController.getInstance().getApis()
                .getTestQuestions(getHeader(), "1", Integer.parseInt(mTestId));
        getQuestionsApi(mCallGetCustomModuleQuestions);
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void getQuestionsApi(Call<GetCustomModuleQuestionsBean> call) {
        call.enqueue(new ResponseHandler<GetCustomModuleQuestionsBean>() {
            @Override
            public void onSuccess(GetCustomModuleQuestionsBean bean) {

                if (bean != null) {
                    if (bean.getStatus() == Constants.SUCCESS_CODE) {
                        mReviewTestAdapter.setList(bean.getData());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetCustomModuleQuestionsBean> call, Throwable t) {
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
