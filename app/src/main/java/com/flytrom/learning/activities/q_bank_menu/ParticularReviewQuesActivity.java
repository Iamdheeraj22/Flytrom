package com.flytrom.learning.activities.q_bank_menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivityParticularReviewQuesBinding;
import com.flytrom.learning.databinding.DialogReportErrorBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ParticularReviewQuesActivity extends BaseActivity<ActivityParticularReviewQuesBinding> {

    private QuestionBean mQuestionBean;
    private Call<SuccessBean> mCallReportQuestion;
    private BaseCustomDialog<DialogReportErrorBinding> mReportErrorDialog;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_particular_review_ques;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        setBaseCallback(baseCallback);
        getData();

    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getSerializable("questionBean") != null) {
                mQuestionBean = (QuestionBean) bundle.getSerializable("questionBean");
                if (mQuestionBean != null) {
                    setQuestionData();
                }
            }
        }
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_close:
                goBack();
                break;
            case R.id.text_report_answer:
                reportErrorDialog();
                break;
        }
    };

    private void setQuestionData() {
        //answer data
        binding.textQuestionOfAnswer.setText(mQuestionBean.getQuestion());
        binding.textRightAnswer.setText(getRightAnswerText(mQuestionBean.getOptions()));
        binding.textWrongAnswer.setText(getWrongAnswer(mQuestionBean));
        binding.textAnswerExplanation.setText(mQuestionBean.getAnswerExplanation());
        binding.textTagId.setText(mQuestionBean.getTagId());

        try {
            if (mQuestionBean.getAnswerFile() != null && !mQuestionBean.getAnswerFile().equals("")) {
                Picasso.with(ParticularReviewQuesActivity.this).load(mQuestionBean.getAnswerFile())
                        .into(binding.imageAnswerFile);
            }
        } catch (Exception ex) {
            binding.imageAnswerFile.setVisibility(View.GONE);
            ex.printStackTrace();
        }
    }


    private String getRightAnswerText(List<OptionsBean> options) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getIsAnswer().equals("Yes"))
                return options.get(i).getName();
        }
        return null;
    }

    private String getWrongAnswer(QuestionBean questionBean) {
        for (int i = 0; i < questionBean.getOptions().size(); i++) {
            if (questionBean.getMcqAnswered() == questionBean.getOptions().get(i).getId()) {
                if (questionBean.getOptions().get(i).getIsAnswer().equals("No")) {
                    binding.relativeWrongAns.setVisibility(View.VISIBLE);
                    return questionBean.getOptions().get(i).getName();
                } else {
                    binding.relativeWrongAns.setVisibility(View.GONE);
                }
            }
        }
        return null;
    }

    private void reportErrorDialog() {
        mReportErrorDialog = new BaseCustomDialog<>(this, R.layout.dialog_report_error, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mReportErrorDialog.dismiss();
                    break;
                case R.id.text_submit:
                    if (mReportErrorDialog.getBinding().editTextComment.getText().length() > 0) {
                        mReportErrorDialog.dismiss();
                        reportErrorApi(mReportErrorDialog.getBinding().editTextComment.getText().toString());
                    }
                    break;
            }
        });
        Objects.requireNonNull(mReportErrorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mReportErrorDialog.setCancelable(true);
        mReportErrorDialog.show();
    }

    private void reportErrorApi(String comments) {
        if (mCallReportQuestion != null) mCallReportQuestion.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("table_id", String.valueOf(mQuestionBean.getId()));
        map.put("comments", comments);
        map.put("type", "answer");
        mCallReportQuestion = AppController.getInstance().getApis().reportContent(getHeader(), map);
        mCallReportQuestion.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast("Reported Successfully");
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                // Log.d("t_message", t.getMessage());
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
