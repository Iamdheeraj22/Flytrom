package com.flytrom.learning.activities.test_menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.flytrom.learning.BuildConfig;
import com.flytrom.learning.R;
import com.flytrom.learning.adapters.TestQuestionAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.bookmark.BookMarkParticularQuestionBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.databinding.ActivitySolveTestChapterBinding;
import com.flytrom.learning.databinding.DialogDeleteModuleBinding;
import com.flytrom.learning.databinding.DialogReportErrorBinding;
import com.flytrom.learning.databinding.ItemQuestionPinPointBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import timber.log.Timber;

public class SolveTestChapterActivity extends BaseActivity<ActivitySolveTestChapterBinding>
        implements BaseCallback {

    private SimpleRecyclerViewAdapter<SolveMCQBean, ItemQuestionPinPointBinding> mPointsAdapter;
    private TestQuestionAdapter testQuestionOptionsAdapter;
    private List<QuestionBean> mCustomModuleQuestionList;
    private List<QuestionBean> mQuestionsList;
    private int mCurrentQuestion = 0;
    private int mCompletedQuestions=0;
    private Call<BookMarkParticularQuestionBean> mCallBookmarkQuestion;
    private Call<SuccessBean> mCallReportQuestion;
    private BaseCustomDialog<DialogDeleteModuleBinding> mConfirmSubmitDialog;
    private BaseCustomDialog<DialogDeleteModuleBinding> mConfirmExitTestDialog;
    private BaseCustomDialog<DialogReportErrorBinding> mReportErrorDialog;
    private String mChapterId, mChapterName, mTestId, mMode, mModuleId, mReportTo, mFrom;
    private List<SolveMCQBean> solveMCQList;
    private Call<SuccessBean> mCallSolve;
    private SolveMCQBean mSolveMcqBean;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_solve_test_chapter;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallBookmarkQuestion != null) mCallBookmarkQuestion.cancel();
        if (mCallSolve != null) mCallSolve.cancel();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (mFrom != null) {
            if (mFrom.equals("CustomModule")) {
               // finishActivityWithBackAnim();
                if (solveMCQList.size() > 0) {
                    if (AppController.getInstance().isInternetOn())
                        confirmExitTestDialog();
                    else {
                        updateSolvedMCQInLocal(solveMCQList);
                    }
                }else{
                    finishActivityWithBackAnim();
                }

            } else {
                if (solveMCQList.size() > 0) {
                    if (AppController.getInstance().isInternetOn())
                        confirmExitTestDialog();
                    else {
                        updateSolvedMCQInLocal(solveMCQList);
                    }
                } else {
                    finishActivityWithBackAnim();
                }
            }
        }
    }

    private void updateSolvedMCQInLocal(List<SolveMCQBean> solveMCQList) {
        if (mFrom != null) {
            if (mFrom.equals("testMenu")) {
                mDBRepositoryKotlin.updateSolvedMCQInLocalTest(solveMCQList, Integer.parseInt(mTestId));
            } else
                mDBRepositoryKotlin.updateSolvedMCQInLocalChapter(solveMCQList, Integer.parseInt(mModuleId));
        }
        goBackScreen();
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
            mChapterId = bundle.getString("question_chapter_id");
            mChapterName = bundle.getString("chapter_name");
            mTestId = bundle.getString("test_id");
            mMode = bundle.getString("mode");
            mFrom = bundle.getString("from");
            if (mMode != null) {
                testQuestionOptionsAdapter.setmMode(mMode);
                if (mMode.equals("0")) binding.relativeQuestionBottom.setVisibility(View.VISIBLE);
            }
            mModuleId = bundle.getString("module_id");
            if (bundle.getSerializable("customModuleQuestionsList") != null) {
                mCustomModuleQuestionList = new ArrayList<>();
                mCustomModuleQuestionList = (List<QuestionBean>)
                        bundle.getSerializable("customModuleQuestionsList");
                mCurrentQuestion=bundle.getInt("completedQuestions");
            }

            if (bundle.getSerializable("chapterQuestionsList") != null) {
                mQuestionsList = new ArrayList<>();
                mQuestionsList = (List<QuestionBean>) bundle.getSerializable("chapterQuestionsList");
                if (mQuestionsList != null) {
                    Log.d("Before Reverse", new Gson().toJson(mQuestionsList));
                    Collections.reverse(mQuestionsList);
                    Log.d("After Reverse", new Gson().toJson(mQuestionsList));
                }
            }

            if (bundle.getSerializable("solveMCQList") != null) {
                solveMCQList = (List<SolveMCQBean>) bundle.getSerializable("solveMCQList");
                if (solveMCQList != null) {
                    mCurrentQuestion = solveMCQList.size();
                }
            }

            if (mFrom.equals("qbMenu")) {

                binding.recyclerPoints.setVisibility(View.VISIBLE);

                List<SolveMCQBean> mcqBeans = new ArrayList<>(solveMCQList);

                for (int i = mcqBeans.size(); i < mQuestionsList.size(); i++) {
                    mcqBeans.add(new SolveMCQBean());
                }

                mPointsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_question_pin_point, BR.bean, (v, solveMCQBean) -> {

                });

                binding.recyclerPoints.setLayoutManager(new GridLayoutManager(this, 30));
                binding.recyclerPoints.setAdapter(mPointsAdapter);
                mPointsAdapter.setList(mcqBeans);

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
            case R.id.image_question_next:

            case R.id.image_answer_next:
                if (mSolveMcqBean != null) {
                    solveMCQList.add(mSolveMcqBean);
                    mSolveMcqBean = null;
                }
                mCurrentQuestion++;
                setQuestionData();
                break;
            case R.id.image_question_previous:
                if (mCurrentQuestion > 0) {
                    mCurrentQuestion--;
                    setQuestionData();
                }
                break;
            case R.id.image_question_bookmark:
            case R.id.image_answer_bookmark:
                if (checkIsInternetOn()) bookmarkQuestion();
                break;
            case R.id.image_question_share:
            case R.id.image_answer_share:
                shareQuestion();
                break;
            case R.id.text_report_answer:
                mReportTo = "answer";
                reportErrorDialog();
                break;
            case R.id.text_report_question:
                mReportTo = "question";
                reportErrorDialog();
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
                        reportError(mReportErrorDialog.getBinding().editTextComment.getText().toString());
                    }
                    break;
            }
        });
        Objects.requireNonNull(mReportErrorDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mReportErrorDialog.setCancelable(true);
        mReportErrorDialog.show();
    }

    private void reportError(String comments) {

        if (mCustomModuleQuestionList != null && mCustomModuleQuestionList.size() > 0) {
            reportErrorApi(mCustomModuleQuestionList.get(mCurrentQuestion).getId(), comments);
        } else {
            if (mQuestionsList != null && mQuestionsList.size() > 0) {
                reportErrorApi(mQuestionsList.get(mCurrentQuestion).getId(), comments);
            }
        }
    }

    private void reportErrorApi(int questionId, String comments) {
        if (mCallReportQuestion != null) mCallReportQuestion.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("table_id", String.valueOf(questionId));
        map.put("comments", comments);
        map.put("type", mReportTo);
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

    private void shareQuestion() {
        QuestionBean bean;
        String message = null;
        if (mCustomModuleQuestionList != null && mCustomModuleQuestionList.size() > 0) {
            bean = mCustomModuleQuestionList.get(mCurrentQuestion);
            if (bean != null) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < bean.getOptions().size(); i++) {
                    builder.append(Constants.abcValues[i]).append(" ").append(bean.getOptions().get(i).getName()).append("\n\n");
                }
                message = "MCQ TAG ID: " + bean.getTagId() + "\n\n"
                        + bean.getQuestion() + "\n\nOPTIONS\n\n"
                        + builder.toString() + "\n" +
                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
            }
        } else {
            if (mQuestionsList != null && mQuestionsList.size() > 0) {
                bean = mQuestionsList.get(mCurrentQuestion);
                if (bean != null) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < bean.getOptions().size(); i++) {
                        builder.append(Constants.abcValues[i]).append(" ").append(bean.getOptions().get(i).getName()).append("\n\n");
                    }
                    message = "MCQ TAG ID: " + bean.getTagId() + "\n\n"
                            + bean.getQuestion() + "\n\nOPTIONS\n\n"
                            + builder.toString() + "\n" +
                            "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                }
            }
        }

        if (message != null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Flytrom\n\n" + message);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    private void bookmarkQuestion() {
        setBookmarkImage(1);
        if (mCustomModuleQuestionList != null && mCustomModuleQuestionList.size() > 0) {
            if (mCurrentQuestion == 0) {
                bookmarkQuestionApi(mCustomModuleQuestionList.get(mCurrentQuestion).getId());
            } else {
                bookmarkQuestionApi(mCustomModuleQuestionList.get(mCurrentQuestion - 1).getId());
            }
        } else {
            if (mQuestionsList != null && mQuestionsList.size() > 0) {
                if (mCurrentQuestion == 0) {
                    bookmarkQuestionApi(mQuestionsList.get(mCurrentQuestion).getId());
                } else {
                    bookmarkQuestionApi(mQuestionsList.get(mCurrentQuestion - 1).getId());
                }
            }
        }
    }

    private void bookmarkQuestionApi(int questionId) {
        if (mCallBookmarkQuestion != null) mCallBookmarkQuestion.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("question_id", String.valueOf(questionId));
        if (mFrom != null) {
            if (mFrom.equals("testMenu"))
                map.put("b_type", "Test");
            else
                map.put("b_type", "QB");
        }
        mCallBookmarkQuestion = AppController.getInstance().getApis().bookmarkQuestion(getHeader(), map);
        mCallBookmarkQuestion.enqueue(new ResponseHandler<BookMarkParticularQuestionBean>() {
            @Override
            public void onSuccess(BookMarkParticularQuestionBean successBean) {

                Timber.d(successBean.getMessage());
                showToast(successBean.getMessage());
                if (successBean.getMessage().equals("Question Bookmarked Successfully")) {
                    setBookmarkImage(1);
                } else {
                    setBookmarkImage(2);
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) Timber.d(t.getMessage());
            }

            @Override
            public void onComplete(Call<BookMarkParticularQuestionBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void setBookmarkImage(int imageType) {
        //imageType = 1 -> Bookmarked
        //imageType = 2 -> Unbookmarked
        switch (imageType) {
            case 1:
                binding.imageQuestionBookmark.setColorFilter(ContextCompat.getColor(SolveTestChapterActivity.this, R.color.blue_text_color),
                        PorterDuff.Mode.SRC_IN);
                binding.imageAnswerBookmark.setColorFilter(ContextCompat.getColor(SolveTestChapterActivity.this, R.color.blue_text_color),
                        PorterDuff.Mode.SRC_IN);
                break;
            case 2:
                binding.imageQuestionBookmark.setColorFilter(ContextCompat.getColor(SolveTestChapterActivity.this, R.color.gray_light),
                        PorterDuff.Mode.SRC_IN);
                binding.imageAnswerBookmark.setColorFilter(ContextCompat.getColor(SolveTestChapterActivity.this, R.color.gray_light),
                        PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setQuestionData() {
        if (mCustomModuleQuestionList != null && mCustomModuleQuestionList.size() > 0) {
            if (mCustomModuleQuestionList.size() > mCurrentQuestion) {
                binding.rlImg.setVisibility(View.GONE);
                binding.rlAns.setVisibility(View.GONE);
                setBookmarkImage(2);
                binding.textCounter.setText(mCurrentQuestion + 1 + " / " + mCustomModuleQuestionList.size());
                binding.textQuestion.setText(mCustomModuleQuestionList.get(mCurrentQuestion).getQuestion());
                try {
                    if (mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile() != null &&
                            !mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile().equals("")) {

                        String imgUrl = mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile();
                        String last4 = takeLast(imgUrl, 4);

                        Log.d("LASTURL",last4);
                        if (last4.equalsIgnoreCase(".gif")){
                            binding.imageQuestionFile.setVisibility(View.GONE);
                            binding.gif.setVisibility(View.VISIBLE);
                            if (imgUrl.substring(0,4).equalsIgnoreCase("http")){
                                Glide.with(this).asGif().load(mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile()).into(binding.gif);
                            }else {
                                Glide.with(this).asGif().load(Constants.MEDIA_URL + mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile()).into(binding.gif);
                            }

                        }else {
                            binding.imageQuestionFile.setVisibility(View.VISIBLE);
                            binding.gif.setVisibility(View.GONE);
                            Glide.with(this).load(Constants.MEDIA_URL + mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile()).into(binding.imageQuestionFile);
                        }
//                        Glide.with(this).load().into(new GlideDrawableImageViewTarget(binding.imageQuestionFile));
//                        Picasso.with(SolveTestChapterActivity.this).load(Constants.MEDIA_URL + mCustomModuleQuestionList.get(mCurrentQuestion).getQuestionFile())
//                                .placeholder(R.drawable.image_test_ques_p_holder)
//                                .into(binding.imageQuestionFile);
                        binding.rlImg.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    binding.rlImg.setVisibility(View.GONE);
                    ex.printStackTrace();
                }

                List<OptionsBean> list = new ArrayList<>(mCustomModuleQuestionList.get(mCurrentQuestion).getOptions());
                prepareAndSetList(list, mCustomModuleQuestionList.get(mCurrentQuestion));

                //answer data
                binding.textCounterOfAnswer.setText(mCurrentQuestion + 1 + " / " + mCustomModuleQuestionList.size());
                binding.textQuestionOfAnswer.setText(mCustomModuleQuestionList.get(mCurrentQuestion).getQuestion());
                binding.textTagId.setText(mCustomModuleQuestionList.get(mCurrentQuestion).getTagId());
                binding.textRightAnswer.setText(getRightAnswerText(mCustomModuleQuestionList.get(mCurrentQuestion).getOptions()));
                binding.textAnswerExplanation.setText(mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerExplanation());

                try {
                    if (mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile() != null &&
                            !mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile().equals("")) {
                        String imgUrl = mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile();
                        String last4 = takeLast(imgUrl, 4);
                        Log.d("LASTURL",last4);
                        if (last4.equalsIgnoreCase(".gif")){
                            binding.imageAnswerFile.setVisibility(View.GONE);
                            binding.gifAns.setVisibility(View.VISIBLE);
                            if (imgUrl.substring(0,4).equalsIgnoreCase("http")){
                                Glide.with(this).asGif().load(mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile()).into(binding.gifAns);
                            }else {
                                Glide.with(this).asGif().load(Constants.MEDIA_URL +mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile()).into(binding.gifAns);
                            }

                        }else {
                            binding.imageAnswerFile.setVisibility(View.VISIBLE);
                            binding.gifAns.setVisibility(View.GONE);
                            Glide.with(this).load(Constants.MEDIA_URL + mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile()).into(binding.imageAnswerFile);
                        }
//                        Picasso.with(SolveTestChapterActivity.this).load(Constants.MEDIA_URL + mCustomModuleQuestionList.get(mCurrentQuestion).getAnswerFile())
//                                .placeholder(R.drawable.image_test_ques_p_holder)
//                                .into(binding.imageAnswerFile);
                        binding.rlAns.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    binding.rlAns.setVisibility(View.GONE);
                    ex.printStackTrace();
                }

                binding.relativeQuestion.setVisibility(View.VISIBLE);
                binding.relativeAnswer.setVisibility(View.GONE);

            } else {
                goNext("CustomModule");
            }
        }
        else {
            if (mQuestionsList != null && mQuestionsList.size() > 0) {
                if (mQuestionsList.size() > mCurrentQuestion) {

                    binding.rlImg.setVisibility(View.GONE);
                    binding.rlAns.setVisibility(View.GONE);
                    setBookmarkImage(2);
                    binding.textCounter.setText(mCurrentQuestion + 1 + " / " + mQuestionsList.size());
                    binding.textQuestion.setText(mQuestionsList.get(mCurrentQuestion).getQuestion());

                    try {
                        if (mQuestionsList.get(mCurrentQuestion).getQuestionFile() != null &&
                                !mQuestionsList.get(mCurrentQuestion).getQuestionFile().equals("")) {
                            String imgUrl = mQuestionsList.get(mCurrentQuestion).getQuestionFile();
                            String last4 = takeLast(imgUrl, 4);
                            Log.d("LASTURL",last4);
                            if (last4.equalsIgnoreCase(".gif")){
                                binding.imageQuestionFile.setVisibility(View.GONE);
                                binding.gif.setVisibility(View.VISIBLE);
                                if (imgUrl.substring(0,4).equalsIgnoreCase("http")){
                                    Glide.with(this).load( mQuestionsList.get(mCurrentQuestion).getQuestionFile()).into(binding.gif);
                                }else {
                                    Glide.with(this).asGif().load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion).getQuestionFile()).into(binding.gif);
                                }
                            }else {
                                binding.imageQuestionFile.setVisibility(View.VISIBLE);
                                binding.gif.setVisibility(View.GONE);
                                Glide.with(this).load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion).getQuestionFile()).into(binding.imageQuestionFile);
                            }
//                            Picasso.with(SolveTestChapterActivity.this).load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion)
//                                    .getQuestionFile())
//                                    .placeholder(R.drawable.image_test_ques_p_holder)
//                                    .into(binding.imageQuestionFile);
                            binding.rlImg.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        binding.rlImg.setVisibility(View.GONE);
                        ex.printStackTrace();
                    }

                    List<OptionsBean> list = new ArrayList<>(mQuestionsList.get(mCurrentQuestion).getOptions());
                    prepareAndSetList(list, mQuestionsList.get(mCurrentQuestion));

                    //answer data
                    binding.textCounterOfAnswer.setText(mCurrentQuestion + 1 + " / " + mQuestionsList.size());
                    binding.textQuestionOfAnswer.setText(mQuestionsList.get(mCurrentQuestion).getQuestion());
                    binding.textTagId.setText(mQuestionsList.get(mCurrentQuestion).getTagId());
                    binding.textRightAnswer.setText(getRightAnswerText(mQuestionsList.get(mCurrentQuestion).getOptions()));
                    binding.textAnswerExplanation.setText(mQuestionsList.get(mCurrentQuestion).getAnswerExplanation());

                    try {
                        if (mQuestionsList.get(mCurrentQuestion).getAnswerFile() != null &&
                                !mQuestionsList.get(mCurrentQuestion).getAnswerFile().equals("")) {
                            String imgUrl = mQuestionsList.get(mCurrentQuestion).getAnswerFile();
                            String last4 = takeLast(imgUrl, 4);
                            Log.d("LASTURL",last4);
                            if (last4.equalsIgnoreCase(".gif")){
                                binding.imageAnswerFile.setVisibility(View.GONE);
                                binding.gifAns.setVisibility(View.VISIBLE);
                                if (imgUrl.substring(0,4).equalsIgnoreCase("http")){
                                    Glide.with(this).asGif().load(mQuestionsList.get(mCurrentQuestion).getAnswerFile()).into(binding.gifAns);
                                }else {
                                    Glide.with(this).asGif().load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion).getAnswerFile()).into(binding.gifAns);
                                }

                            }else {
                                binding.imageAnswerFile.setVisibility(View.VISIBLE);
                                binding.gifAns.setVisibility(View.GONE);
                                Glide.with(this).load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion).getAnswerFile()).into(binding.imageAnswerFile);
                            }
//                            Picasso.with(SolveTestChapterActivity.this).load(Constants.MEDIA_URL + mQuestionsList.get(mCurrentQuestion)
//                                    .getAnswerFile())
//                                    .placeholder(R.drawable.image_test_ques_p_holder)
//                                    .into(binding.imageAnswerFile);
                            binding.rlAns.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        binding.rlAns.setVisibility(View.GONE);
                        ex.printStackTrace();
                    }
                    binding.relativeQuestion.setVisibility(View.VISIBLE);
                    binding.relativeAnswer.setVisibility(View.INVISIBLE);

                    if (mFrom.equals("testMenu")) {
                        if (mCurrentQuestion == 0) {
                            binding.imageQuestionPrevious.setVisibility(View.GONE);
                        } else {
                            binding.imageQuestionPrevious.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    goNext("ChapterTest");
                }
            }
        }
    }

    private void confirmSubmitDialog(String fromScreen) {
        mConfirmSubmitDialog = new BaseCustomDialog<>(this, R.layout.dialog_delete_module, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmSubmitDialog.dismiss();
                    break;
                case R.id.text_delete:
                    mConfirmSubmitDialog.dismiss();
                    if (AppController.getInstance().isInternetOn())
                        solveApi(fromScreen);
                    else goNextScreen(fromScreen);
                    break;
            }
        });
        Objects.requireNonNull(mConfirmSubmitDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mConfirmSubmitDialog.getBinding().textMessage.setText("You result will be calculated on the basis of your selections");
        mConfirmSubmitDialog.getBinding().textDelete.setText("Submit");
        mConfirmSubmitDialog.getBinding().imageIcon.setImageResource(R.drawable.ic_completed);
        mConfirmSubmitDialog.setCancelable(true);
        mConfirmSubmitDialog.show();
    }

    private void confirmExitTestDialog() {
        mConfirmExitTestDialog = new BaseCustomDialog<>(this, R.layout.dialog_delete_module, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmExitTestDialog.dismiss();
                    break;
                case R.id.text_delete:
                    mConfirmExitTestDialog.dismiss();
                    if (AppController.getInstance().isInternetOn())
                        solveApi(null);
                    break;
            }
        });
        Objects.requireNonNull(mConfirmExitTestDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mConfirmExitTestDialog.getBinding().textMessage.setText(R.string.text_want_to_exit_flow);
        mConfirmExitTestDialog.getBinding().textDelete.setText(getString(R.string.exit));
        mConfirmExitTestDialog.setCancelable(true);
        mConfirmExitTestDialog.show();
    }

    private String getRightAnswerText(List<OptionsBean> options) {
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).getIsAnswer().equals("Yes"))
                return options.get(i).getName();
        }
        return null;
    }

    private void goNext(String fromScreen) {

        if (mCustomModuleQuestionList != null && mCustomModuleQuestionList.size() > 0) {
            if (fromScreen.equals("CustomModule")) {
                confirmSubmitDialog(fromScreen);
            }
        } else {
            if (mQuestionsList != null && mQuestionsList.size() > 0) {
                if (solveMCQList != null && solveMCQList.size() > 0) {
                    confirmSubmitDialog(fromScreen);
                } else {
                    showToast("Please answer questions");
                }
            }
        }
    }

    private void solveApi(String fromScreen) {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("answers", new Gson().toJson(solveMCQList));
        map.put("is_completed", fromScreen == null ? "0" : "1");
        if (mCustomModuleQuestionList == null) {
            if (mChapterId != null) {
                map.put("chapter_id", mChapterId);
                mCallSolve = AppController.getInstance().getApis().solveMCQApi(getHeader(), map);
            } else {
                if (mTestId != null) {
                    map.put("test_id", mTestId);
                    mCallSolve = AppController.getInstance().getApis().solveTest(getHeader(), map);
                }
            }
        } else {
            mCallSolve = AppController.getInstance().getApis().solveMCQApi(getHeader(), map);
        }

        mCallSolve.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (fromScreen != null)
                            goNextScreen(fromScreen);
                        else {
                            goBackScreen();
                        }
                    }
                }
            }
            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null)
                    Timber.e(t.getMessage());
                    showToast(t.getMessage());
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

    private void goBackScreen() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("solveMCQList", (Serializable) solveMCQList);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivityWithBackAnim();
    }

    private void goNextScreen(String fromScreen) {
        updateSolvedMCQInLocal(new ArrayList<>());
        if (mCustomModuleQuestionList == null) {
            goToRateScreen(fromScreen, mQuestionsList.size());
        } else {
            goToRateScreen(fromScreen, mCustomModuleQuestionList.size());
        }
    }

    private void goToRateScreen(String fromScreen, int totalQuestions) {

        Bundle bundle = new Bundle();
        bundle.putString("fromScreen", fromScreen);
        bundle.putInt("totalQuestions", totalQuestions);
        bundle.putInt("correctCount", getCorrectCount());
        if (mChapterId != null)
            bundle.putString("question_chapter_id", mChapterId);
        if (mChapterName != null)
            bundle.putString("chapter_name", mChapterName);
        if (mTestId != null)
            bundle.putString("test_id", mTestId);
        if (mModuleId != null)
            bundle.putString("module_id", mModuleId);
        Intent intent = new Intent(SolveTestChapterActivity.this, RateContentActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        goNextAnimation();
        finish();
    }

    private int getCorrectCount() {
        int correctCounter = 0;
        for (int i = 0; i < solveMCQList.size(); i++) {
            if (solveMCQList.get(i).getWasCorrect() == 1) {
                correctCounter += 1;
            }
        }
        return correctCounter;
    }


    private void prepareAndSetList(List<OptionsBean> list, QuestionBean questionBean) {

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIndex(i);
            list.get(i).setSelectedIndex(-1);
        }

        if (mFrom.equals("testMenu")) {
            for (int j = 0; j < solveMCQList.size(); j++) {
                if (questionBean.getId() == solveMCQList.get(j).getQuestionId()) {

                    for (int k = 0; k < questionBean.getOptions().size(); k++) {
                        if (questionBean.getOptions().get(k).getId() == solveMCQList.get(j).getQuestionOptionId()) {
                            list.get(k).setSelected(true);
                            break;
                        }
                    }
                }
            }
        }

        testQuestionOptionsAdapter.setDataList(list);
        binding.recyclerViewOptions.scheduleLayoutAnimation();

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onViewClick(View view, int position) {

        if (mMode.equals("1")) {
            if (testQuestionOptionsAdapter.getData().get(position).getSelectedIndex() == -1) {
                setSelectedIndex(position);
                if (testQuestionOptionsAdapter.getData().get(position).getIsAnswer().equals("Yes")) {

                    if (mFrom.equals("qbMenu")) {
                        mPointsAdapter.getList().get(mCurrentQuestion).setUser_id("dummy");
                        mPointsAdapter.getList().get(mCurrentQuestion).setWasCorrect(1);
                    }
                    binding.relativeWrongAns.setVisibility(View.GONE);
                } else {
                    if (mFrom.equals("qbMenu")) {
                        mPointsAdapter.getList().get(mCurrentQuestion).setUser_id("dummy");
                        mPointsAdapter.getList().get(mCurrentQuestion).setWasCorrect(0);
                    }
                    if (PrefUtils.getInstance().isVibrationModeOn()) {
                        if (vibrator != null) vibrator.vibrate(100);
                    }
                    binding.relativeWrongAns.setVisibility(View.VISIBLE);
                    binding.textWrongAnswer.setText(testQuestionOptionsAdapter.getData().get(position).getName());
                }

                if (mFrom.equals("qbMenu")) mPointsAdapter.notifyItemChanged(mCurrentQuestion);

                prepareRegularModeJson(position);

                new Handler().postDelayed(() -> {
                    binding.relativeQuestion.setVisibility(View.GONE);
                    binding.relativeAnswer.setVisibility(View.VISIBLE);
                }, 1000);
            }
        } else {

            for (int i = 0; i < testQuestionOptionsAdapter.getData().size(); i++) {
                testQuestionOptionsAdapter.getData().get(i).setSelected(false);
            }
            testQuestionOptionsAdapter.getData().get(position).setSelected(true);

            prepareExamModeJson(position);
        }
        testQuestionOptionsAdapter.notifyDataSetChanged();
    }

    private void prepareRegularModeJson(int selectedOptionPosition) {
        SolveMCQBean solveMCQBean = new SolveMCQBean();
        solveMCQBean.setQuestionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getQuestionId());
        solveMCQBean.setQuestionOptionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getId());
        solveMCQBean.setUser_id("dummy");
        if (testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getIsAnswer().equals("Yes"))
            solveMCQBean.setWasCorrect(1);
        else
            solveMCQBean.setWasCorrect(0);

        solveMCQList.add(solveMCQBean);
    }

    private void prepareExamModeJson(int selectedOptionPosition) {
        mSolveMcqBean = new SolveMCQBean();
        mSolveMcqBean.setQuestionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getQuestionId());
        mSolveMcqBean.setQuestionOptionId(testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getId());
        mSolveMcqBean.setUser_id("dummy");
        if (testQuestionOptionsAdapter.getData().get(selectedOptionPosition).isSelected() &&
                testQuestionOptionsAdapter.getData().get(selectedOptionPosition).getIsAnswer().equals("Yes")) {
            mSolveMcqBean.setWasCorrect(1);
        } else {
            mSolveMcqBean.setWasCorrect(0);
        }
    }

    public static String takeLast(String value, int count) {
        if (value == null || value.trim().length() == 0 || count < 1) {
            return "";
        }

        if (value.length() > count) {
            return value.substring(value.length() - count);
        } else {
            return value;
        }
    }
}
