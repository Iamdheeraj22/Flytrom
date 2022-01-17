package com.flytrom.learning.activities.q_bank_menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.TestQuestionAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivityParticularQuesBinding;
import com.flytrom.learning.databinding.DialogReportErrorBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ParticularQuestionActivity extends BaseActivity<ActivityParticularQuesBinding>
        implements BaseCallback {

    private TestQuestionAdapter testQuestionOptionsAdapter;
    private QuestionBean mQuestionBean;
    private List<SolveMCQBean> solveMCQList;
    private Call<SuccessBean> mCallReportQuestion;
    private BaseCustomDialog<DialogReportErrorBinding> mReportErrorDialog;
    private String mFrom;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_particular_ques;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallReportQuestion != null) mCallReportQuestion.cancel();
        super.onDestroy();
    }

    private void initView() {
        solveMCQList = new ArrayList<>();
        testQuestionOptionsAdapter = new TestQuestionAdapter(this, this);
        binding.recyclerViewOptions.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewOptions.setAdapter(testQuestionOptionsAdapter);
        setBaseCallback(baseCallback);
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getSerializable("questionBean") != null) {
                mQuestionBean = (QuestionBean) bundle.getSerializable("questionBean");
                mFrom = bundle.getString("action");
            }
        }

        setQuestionData();

    }

    private void setSelectedIndex(int index) {
        for (int i = 0; i < testQuestionOptionsAdapter.getData().size(); i++) {
            testQuestionOptionsAdapter.getData().get(i).setSelectedIndex(testQuestionOptionsAdapter.getData().get(index).getIndex());
        }

        testQuestionOptionsAdapter.notifyDataSetChanged();
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_close:
                goBack();
                break;
            case R.id.text_report_question:
                if (mQuestionBean != null) reportErrorDialog();
                break;
        }
    };

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

    private void setQuestionData() {
        if (mQuestionBean != null) {

            binding.imageBookmark.setColorFilter(ContextCompat.getColor(ParticularQuestionActivity.this, R.color.gray_light),
                    PorterDuff.Mode.SRC_IN);
            binding.textQuestion.setText(mQuestionBean.getQuestion());

            try {
                if (mQuestionBean.getQuestionFile() != null && !mQuestionBean.getQuestionFile().equals("")) {
                    Picasso.with(ParticularQuestionActivity.this)
                            .load(Constants.MEDIA_URL + mQuestionBean.getQuestionFile())
                            .placeholder(R.drawable.image_test_ques_p_holder)
                            .into(binding.imageQuestionFile);
                    binding.imageQuestionFile.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                binding.imageQuestionFile.setVisibility(View.GONE);
                ex.printStackTrace();
            }

            List<OptionsBean> list = new ArrayList<>(mQuestionBean.getOptions());
            prepareAndSetList(list);


            //answer data

            binding.textQuestionOfAnswer.setText(mQuestionBean.getQuestion());
            binding.textRightAnswer.setText(getRightAnswerText(mQuestionBean.getOptions()));
            binding.textWrongAnswer.setText(getWrongAnswer(mQuestionBean.getOptions()));
            binding.textAnswerExplanation.setText(mQuestionBean.getAnswerExplanation());
            binding.textTagId.setText(mQuestionBean.getTagId());

            if (mFrom.equals("home")) {
                binding.relativeQuestion.setVisibility(View.GONE);
                binding.relativeAnswer.setVisibility(View.VISIBLE);
            } else {
                binding.relativeQuestion.setVisibility(View.VISIBLE);
                binding.relativeAnswer.setVisibility(View.GONE);
            }

            try {
                if (mQuestionBean.getAnswerFile() != null && !mQuestionBean.getAnswerFile().equals("")) {
                    Picasso.with(ParticularQuestionActivity.this)
                            .load(Constants.MEDIA_URL + mQuestionBean.getAnswerFile())
                            .placeholder(R.drawable.image_test_ques_p_holder)
                            .into(binding.imageQuestionFile);
                    binding.imageAnswerFile.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                binding.imageAnswerFile.setVisibility(View.GONE);
                ex.printStackTrace();
            }
        }
    }

    private String getWrongAnswer(List<OptionsBean> options) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getHasAnswered() != null) {
                if (options.get(i).getHasAnswered().equals("1")) {
                    if (options.get(i).getIsAnswer().equals("No")) {
                        binding.relativeWrongAns.setVisibility(View.VISIBLE);
                        return options.get(i).getName();
                    } else {
                        binding.relativeWrongAns.setVisibility(View.GONE);
                    }
                }
            }
        }
        return null;
    }

    private String getRightAnswerText(List<OptionsBean> options) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getIsAnswer().equals("Yes"))
                return options.get(i).getName();
        }
        return null;
    }

    private void prepareAndSetList(List<OptionsBean> list) {


        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIndex(i);
            list.get(i).setSelectedIndex(-1);
        }
        testQuestionOptionsAdapter.setDataList(list);
        testQuestionOptionsAdapter.setmMode("1");
        binding.recyclerViewOptions.scheduleLayoutAnimation();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewClick(View view, int position) {
        if (testQuestionOptionsAdapter.getData().get(position).getSelectedIndex() == -1) {
            setSelectedIndex(position);
            /*Vibrator vibrator = (Vibrator) Objects.requireNonNull(this).getSystemService(Context.VIBRATOR_SERVICE);
            if (vibrator != null) vibrator.vibrate(100);*/

            if (testQuestionOptionsAdapter.getData().get(position).getIsAnswer().equals("Yes")) {
                binding.relativeWrongAns.setVisibility(View.GONE);
            } else {
                binding.relativeWrongAns.setVisibility(View.VISIBLE);
                binding.textWrongAnswer.setText(testQuestionOptionsAdapter.getData().get(position).getName());
            }

            new Handler().postDelayed(() -> {
                binding.relativeQuestion.setVisibility(View.GONE);
                binding.relativeAnswer.setVisibility(View.VISIBLE);
            }, 1000);
        }
    }

    private void prepareJson(int selectedOptionPosition) {
        SolveMCQBean solveMCQBean = new SolveMCQBean();
        solveMCQBean.setQuestionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getQuestionId());
        solveMCQBean.setQuestionOptionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getId());
        if (testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getIsAnswer().equals("Yes"))
            solveMCQBean.setWasCorrect(1);
        else
            solveMCQBean.setWasCorrect(0);

        solveMCQList.add(solveMCQBean);
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

                Log.d("t_message", t.getMessage());
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
