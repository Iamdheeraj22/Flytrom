package com.flytrom.learning.activities.test_menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.beans.response_beans.test_bean.TestBean;
import com.flytrom.learning.databinding.ActivityGoingToStartTestBinding;
import com.flytrom.learning.databinding.DialogDeleteModuleBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.ResponseHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class GoingToStartTestActivity extends BaseActivity<ActivityGoingToStartTestBinding> {

    private ChapterBean mChapterBean;
    private TestBean mTestBean;
    private Call<GetCustomModuleQuestionsBean> mCallGetQuestions;
    private List<QuestionBean> mQuestionsList;
    private String mFrom;
    int mTestId = 0, mChapterId = 0;
    private BaseCustomDialog<DialogDeleteModuleBinding> mResetTestDialog;
    private Call<SuccessBean> mCallResetTest;
    private List<SolveMCQBean> solveMCQList;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_going_to_start_test;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallGetQuestions != null) mCallGetQuestions.cancel();
        if (mCallResetTest != null) mCallResetTest.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("solveMCQList", (Serializable) solveMCQList);
        if (mTestBean != null) {
            bundle.putInt("position", mTestBean.getPosition());
        } else {
            bundle.putInt("position", mChapterBean.getPosition());
        }
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivityWithBackAnim();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.TEST_DETAILS:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            solveMCQList = (List<SolveMCQBean>) bundle.getSerializable("solveMCQList");
                            Collections.reverse(solveMCQList);
                            if (solveMCQList != null && solveMCQList.size() > 0) {
                                if (mTestBean != null) {
                                    mTestBean.setSavedData(solveMCQList);
                                }
                                binding.textCompleted.setText(solveMCQList.size() + " " + getString(R.string.completed));
                                MySharedPreferences.getInstance().saveString(this, ConstantsNew.TEST_COMPLETE, String.valueOf(solveMCQList.size()));
                            }
                        }
                    }
                }
                break;
            case Constants.CHAPTER_DETAILS:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            solveMCQList = (List<SolveMCQBean>) bundle.getSerializable("solveMCQList");
                            Collections.reverse(solveMCQList);
                            if (solveMCQList != null && solveMCQList.size() > 0) {
                                if (mChapterBean != null) {
                                    mChapterBean.setSavedData(solveMCQList);
                                }
                                binding.textCompleted.setText(solveMCQList.size() + " " + getString(R.string.completed));
                            }
                        }
                    }
                }
                break;
        }
    }

    private void initView() {
        solveMCQList = new ArrayList<>();
        mQuestionsList = new ArrayList<>();
        setBaseCallback(baseCallback);
        getData();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFrom = bundle.getString("from");
            mChapterBean = (ChapterBean) bundle.getSerializable("chapterBean");
            mTestBean = (TestBean) bundle.getSerializable("testBean");
            if (mTestBean == null){
                Glide.with(this).load(Constants.MEDIA_URL + mChapterBean.getIcon()).into(binding.iconTest);
            }else {
                Glide.with(this).load(Constants.MEDIA_URL + mTestBean.getIcon()).into(binding.iconTest);
            }

            if (mChapterBean != null && mChapterBean.getQuestion_category() != null) {
                binding.toolbar.textTitle.setText(mChapterBean.getQuestion_category());
                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                binding.textChapterTitle.setText(mChapterBean.getTitle());
                if (mChapterBean.getSavedData() != null && mChapterBean.getSavedData().size() > 0) {
                    binding.textCompleted.setText(mChapterBean.getSavedData().size() + " " + getString(R.string.completed));
                    binding.buttonAction.setText(getString(R.string._continue));
                }
                binding.textTotalMcq.setText(mChapterBean.getTotal_mcqs() + " MCQs");

                if (mChapterBean.getAverage_rating().equals("0"))
                    binding.textRateValue.setText("5");
                else
                    binding.textRateValue.setText(mChapterBean.getAverage_rating());

                if (mChapterBean.getIs_completed() != null) {
                    if (mChapterBean.getIs_completed().equals("1")) {
                        binding.buttonAction.setText(getString(R.string.review_caps));
                        binding.textCompleted.setText(getString(R.string.all_completed));
                    } else
                        binding.textResetTest.setVisibility(View.VISIBLE);
                }
            } else {
                if (mTestBean != null) {
                    binding.toolbar.textTitle.setText(mTestBean.getSubject());
                    binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                    binding.textChapterTitle.setText(mTestBean.getTestName());
                    if (mTestBean.getSavedData() != null && mTestBean.getSavedData().size() > 0) {
                        binding.textCompleted.setText(mTestBean.getSavedData().size() + " " + getString(R.string.completed));
                        binding.buttonAction.setText(getString(R.string._continue));
                    }
                    binding.textTotalMcq.setText(mTestBean.getTotalQuestions() + " " + getString(R.string.mcqs));

                    if (mTestBean.getAverageRating().equals("0"))
                        binding.textRateValue.setText("5");
                    else
                        binding.textRateValue.setText(mTestBean.getAverageRating());

                    if (mTestBean.getIsCompleted().equals("1")) {
                        binding.textCompleted.setText(getString(R.string.all_completed));
                        binding.buttonAction.setText(getString(R.string.review_caps));
                    } else
                        binding.textResetTest.setVisibility(View.VISIBLE);
                }
            }

            if (mTestBean != null) {
                getTestQuestionsFromRoom(String.valueOf(mTestBean.getId()));
            } else {
                if (mChapterBean != null) {
                    getQBankChapterQuestionsFromRoom(mChapterBean.getTitle(), mChapterBean.getId());
                }
            }
            if (AppController.getInstance().isInternetOn()) getQuestions();
        }
    }


    private BaseCallback baseCallback = view -> {

        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_action:
                goNextScreen();
                break;
            case R.id.text_reset_test:
                showResetTestDialog();
                break;
        }
    };

    private void showResetTestDialog() {
        mResetTestDialog = new BaseCustomDialog<>(this, R.layout.dialog_delete_module, new BaseCustomDialog.DialogListener() {
            @Override
            public void onViewClick(View view) {
                switch (view.getId()) {
                    case R.id.text_cancel:
                        mResetTestDialog.dismiss();
                        break;
                    case R.id.text_delete:
                        mResetTestDialog.dismiss();
                        resetApi();
                        break;
                }
            }
        });
        Objects.requireNonNull(mResetTestDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mResetTestDialog.getBinding().textMessage.setText("You will not be able to recover this test again!");
        mResetTestDialog.getBinding().textDelete.setText("Reset");
        mResetTestDialog.setCancelable(true);
        mResetTestDialog.show();
    }

    private void resetApi() {
        showBaseProgress();
        if (mTestBean != null)
            mCallResetTest = AppController.getInstance().getApis().resetTest(getHeader(), mTestBean.getId());
        else
            mCallResetTest = AppController.getInstance().getApis().resetQbankChapter(getHeader(), mChapterBean.getId());

        mCallResetTest.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {

                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());

                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        solveMCQList.clear();
                        bundle.putSerializable("solveMCQList", (Serializable) solveMCQList);
                        if (mTestBean != null) {
                            bundle.putInt("position", mTestBean.getPosition());
                            bundle.putBoolean("reset", true);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                        } else {
                            bundle.putInt("position", mChapterBean.getPosition());
                            bundle.putBoolean("reset", true);
                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                        }
                        finishActivityWithBackAnim();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showToast(t.getMessage());
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

    private void goNextScreen() {
        if (mTestBean != null) {
            if (mTestBean.getIsCompleted() != null) {
                if (mTestBean.getIsCompleted().equals("1"))
                    goToReviewScreen();
                else
                    goToTestScreen();
            }
        } else {
            if (mChapterBean.getIs_completed() != null) {
                if (mChapterBean.getIs_completed().equals("1"))
                    goToReviewScreen();
                else
                    goToTestScreen();
            }
        }
    }

    private void goToReviewScreen() {
        Bundle bundle = new Bundle();
        if (mTestBean != null)
            bundle.putString("test_id", String.valueOf(mTestBean.getId()));
        if (mChapterBean != null) {
            bundle.putString("question_chapter_id", String.valueOf(mChapterBean.getId()));
            bundle.putString("chapter_name", mChapterBean.getTitle());
        }
        startActivity(new Intent(GoingToStartTestActivity.this, ReviewTestActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private void goToTestScreen() {
        if (mQuestionsList.size() > 0) {
            Bundle bundle = new Bundle();
            bundle.putString("from", mFrom);
            bundle.putSerializable("chapterQuestionsList", (Serializable) mQuestionsList);

            Intent intent = new Intent(GoingToStartTestActivity.this, SolveTestChapterActivity.class);

            if (mChapterBean != null) {
                bundle.putString("question_chapter_id", String.valueOf(mChapterBean.getId()));
                bundle.putString("chapter_name", mChapterBean.getTitle());
                bundle.putString("mode", "1");
                bundle.putSerializable("solveMCQList", (Serializable) mChapterBean.getSavedData());
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.CHAPTER_DETAILS);
            }

            if (mTestBean != null) {
                bundle.putString("test_id", String.valueOf(mTestBean.getId()));
                bundle.putString("mode", "0");
                bundle.putSerializable("solveMCQList", (Serializable) mTestBean.getSavedData());
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.TEST_DETAILS);
            }

            goNextAnimation();
        }
    }

    private void getQuestions() {
        if (mFrom != null && mFrom.equals("testMenu")) {
            if (mTestBean != null) {
                mTestId = mTestBean.getId();
                mCallGetQuestions = AppController.getInstance().getApis().getTestQuestions(getHeader(), "1", mTestBean.getId());
            }
        } else {
            mChapterId = mChapterBean.getId();
            if (mChapterBean.getType().equals(Constants.PAID)) {
                mCallGetQuestions = AppController.getInstance().getApis().getChapterQuestions(getHeader(), "1", mChapterBean.getTitle(), Constants.PAID);
            } else {
                mCallGetQuestions = AppController.getInstance().getApis().getChapterQuestions(getHeader(), "1", mChapterBean.getTitle(), Constants.FREE);
            }
        }
        mCallGetQuestions.enqueue(new ResponseHandler<GetCustomModuleQuestionsBean>() {
            @Override
            public void onSuccess(GetCustomModuleQuestionsBean getChapterQuestionsBean) {
                if (getChapterQuestionsBean != null) {
                    if (getChapterQuestionsBean.getStatus() == Constants.SUCCESS_CODE) {
                        mQuestionsList = getChapterQuestionsBean.getData();
                        Collections.reverse(mQuestionsList);
                        insertQuestion(getChapterQuestionsBean.getData());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetCustomModuleQuestionsBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void insertQuestion(List<QuestionBean> list) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setTestId(mTestId);
            list.get(i).setChapterId(mChapterId);
        }
        mDBRepositoryKotlin.insertQuestions(list);
    }

    private void getTestQuestionsFromRoom(String id) {
        mDBRepository.getTestQuestions(id).observe(this, list -> {
            if (list.size() > 0) mQuestionsList = list;
        });
    }

    private void getQBankChapterQuestionsFromRoom(String questionCategoryChapter, int mChapterId) {
        mDBRepository.getQBankChapterQuestions(questionCategoryChapter, mChapterId).observe(this, list -> {
            if (list.size() > 0) mQuestionsList = list;
        });
    }
}
