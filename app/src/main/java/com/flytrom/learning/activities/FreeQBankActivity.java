package com.flytrom.learning.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.test_menu.GoingToStartTestActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.q_bank.GetQuestionBankChapterBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.videos.GetVideosBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivityFreeQbankBinding;
import com.flytrom.learning.databinding.ActivityFreeVideosBinding;
import com.flytrom.learning.databinding.DialogConfirmBinding;
import com.flytrom.learning.databinding.ItemFreeVideosBinding;
import com.flytrom.learning.databinding.ItemParticularSubjectsChapterBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class FreeQBankActivity extends BaseActivity<ActivityFreeQbankBinding>
        implements VerticalPagination.VerticalScrollListener{

    private SimpleRecyclerViewAdapter<ChapterBean, ItemParticularSubjectsChapterBinding> mChaptersAdapter;
    private SubjectBean mSubjectBean;
    private Call<GetQuestionBankChapterBean> mCallChapters;
    private Call<GetCustomModuleQuestionsBean> mCallGetQuestions;
    private Call<SuccessBean> mCallMarkDownload;
    private VerticalPagination mVerticalPagination;
    private BaseCustomDialog<DialogConfirmBinding> mConfirmDialog;
    private List<SolveMCQBean> solveMCQList;
    private int mCurrentPage = 1;
    private String mChapterTypes,image;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Intent intent1 = getIntent();
        binding.toolbar.textTitle.setText("Free QBank");
        initView();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }
    @Override
    protected int getContentView() {
        return R.layout.activity_free_qbank;
    }

    @Override
    public void onDestroy() {
        if (mCallChapters != null) mCallChapters.cancel();
        if (mCallGetQuestions != null) mCallGetQuestions.cancel();
        if (mCallMarkDownload != null) mCallMarkDownload.cancel();
        super.onDestroy();
    }
    @Override
    public void onLoadMore() {
        getAllChapters(mCurrentPage, false);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mChapterTypes.equals(Constants.PAID)) {
            if (requestCode == Constants.CHAPTER_ITEM) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle != null) {
                            int position = bundle.getInt("position");
                            ChapterBean chapterBean = mChaptersAdapter.getList().get(position);
                            if (bundle.getBoolean("reset")) {
                                chapterBean.setSavedData(null);
                                chapterBean.setIs_completed("0");
                                chapterBean.setStatus(Constants.CONTENT_STATUS[3]);
                            }

                            solveMCQList = (List<SolveMCQBean>) bundle.getSerializable("solveMCQList");
                            if (solveMCQList != null && solveMCQList.size() > 0) {
                                chapterBean.setSavedData(solveMCQList);
                                chapterBean.setStatus(Constants.CONTENT_STATUS[1]);
                            }

                            mChaptersAdapter.getList().set(position, chapterBean);
                            mChaptersAdapter.notifyItemChanged(position);
                        }
                    }
                }
            }
        }
    }

    private void initView() {
        solveMCQList = new ArrayList<>();
        mChaptersAdapter = new SimpleRecyclerViewAdapter<>(R.layout.q_bank_play_list, BR.bean,
                new SimpleRecyclerViewAdapter.SimpleCallback<ChapterBean>() {
            @Override
            public void onClick(View v, ChapterBean chapterBean) {

            }

            @Override
            public void onClickWithPosition(View v, ChapterBean chapterBean, int pos) {

                if (PrefUtils.getInstance().getSelectedSubject() != null)
                    showPurchasePlanDialog(getString(R.string.chapter));
                else {
                    switch (v.getId()) {
                        case R.id.btn_download:
                            if (checkIsInternetOn()) {
                                chapterBean.setPosition(pos);
                                downloadTest(chapterBean);
                            }
                            break;
                        case R.id.relative_chapter:
                            chapterBean.setPosition(pos);
                            goToGoingToStartTest(chapterBean);
                            break;
                    }
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerFreeVideosQBank.setLayoutManager(linearLayoutManager);
        binding.recyclerFreeVideosQBank.addOnScrollListener(mVerticalPagination);
        binding.recyclerFreeVideosQBank.setAdapter(mChaptersAdapter);

        setBaseCallback(baseCallback);
        getData();
    }

    private void downloadTest(ChapterBean chapterBean) {
        if (AppController.getInstance().isInternetOn()) {
            if (chapterBean.getIs_downloaded().equals("No")) {
                downloadChapterQuestions(chapterBean);
            } else {
                deleteDownloadedTestDialog(chapterBean);
            }
        } else
            showToast(getString(R.string.no_internet));
    }

    private void deleteDownloadedTestDialog(ChapterBean chapterBean) {
        mConfirmDialog = new BaseCustomDialog<>(this, R.layout.dialog_confirm, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmDialog.dismiss();
                    break;
                case R.id.text_yes:
                    mConfirmDialog.dismiss();
                    setDownloaded(chapterBean, "No");
                    markChapterDownloaded(chapterBean);
                    break;
            }
        });
        Objects.requireNonNull(mConfirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mConfirmDialog.getBinding().textMessage.setText(R.string.title_remove_downloaded_chapter);
        mConfirmDialog.getBinding().imageIcon.setImageResource(R.drawable.ic_alert);
        mConfirmDialog.getBinding().textYes.setText(R.string.text_yes);
        mConfirmDialog.setCancelable(true);
        mConfirmDialog.show();
    }

    private void setDownloaded(ChapterBean chapterBean, String type) {
        mChaptersAdapter.getList().get(chapterBean.getPosition()).setIs_downloaded(type);
        mChaptersAdapter.notifyItemChanged(chapterBean.getPosition());

        chapterBean.setIs_downloaded(type);
        mDBRepositoryKotlin.updateDownloadStatusOfChapter(chapterBean);
    }

    private void markChapterDownloaded(ChapterBean chapterBean) {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(chapterBean.getId()));
        map.put("type", "question_bank_chapters");
        mCallMarkDownload = AppController.getInstance().getApis().markDownload(getHeader(), map);
        mCallMarkDownload.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        //showToast("");
                        //showSuccessToast(successBean.getMessage());
                        //getTests(mCurrentPage, true);
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

    private void goToGoingToStartTest(ChapterBean chapterBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("chapterBean", chapterBean);
        bundle.putString("from", "qbMenu");
        startActivityForResult(new Intent(this, GoingToStartTestActivity.class)
                .putExtras(bundle), Constants.CHAPTER_ITEM);
        goNextAnimation();
    }

    private void getData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSubjectBean = (SubjectBean) bundle.getSerializable("subjectBean");
            if (mSubjectBean != null) {
//                binding.lectureNameQbank.setText(mSubjectBean.getTitle());
//                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
            mChapterTypes = bundle.getString("type");

            if (mChapterTypes != null) {
                if (mChapterTypes.equals(Constants.FREE)){
//                    binding.lectureNameQbank.setText(getString(R.string.text_free_questions));
                }
//                    binding.toolbar.textTitle.setText(getString(R.string.text_free_questions));
//                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
        }

        if (mSubjectBean != null) {
            getSubjectChaptersFromRoom(mSubjectBean.getTitle());
        } else {
            getFreeChaptersFromRoom();
        }
        if (AppController.getInstance().isInternetOn()) getAllChapters(mCurrentPage, true);

    }

    private void getAllChapters(int currentPage, boolean progress) {
            mCallChapters = AppController.getInstance().getApis().getChapters(getHeader(),
                    String.valueOf(currentPage), null, Constants.FREE);
        mCallChapters.enqueue(new ResponseHandler<GetQuestionBankChapterBean>() {
            @Override
            public void onSuccess(GetQuestionBankChapterBean bankChapterBean) {

                if (bankChapterBean != null) {
                    if (bankChapterBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (bankChapterBean.getData() != null && bankChapterBean.getData().size() > 0) {

                           /* for (int i = 0; i < bankChapterBean.getData().size(); i++) {
                                bankChapterBean.getData().get(i).setTitle_category(mSubjectBean.getTitle());}

                           */

                            for (int i = 0; i < bankChapterBean.getData().size(); i++) {
                                if (bankChapterBean.getData().get(i).getTotal_mcqs().equalsIgnoreCase("0")){
                                    bankChapterBean.getData().get(i).setProgress(String.valueOf(0));
                                }else {
                                    if (bankChapterBean.getData().get(i).getSavedData().size() != 0){
                                        float totalQBank = Integer.parseInt(bankChapterBean.getData().get(i).getTotal_mcqs());
                                        float totalQBankComplete = bankChapterBean.getData().get(i).getSavedData().size();
                                        float res = (totalQBankComplete / totalQBank) * 100;
                                        int total = Math.round(res);
                                        Log.d("totalQBank", String.valueOf(total));
                                        bankChapterBean.getData().get(i).setProgress(String.valueOf(total));
                                    }else {
                                        bankChapterBean.getData().get(i).setProgress(String.valueOf(0));
                                    }

                                }

                            }

                            if (mCurrentPage == 1)
                                mChaptersAdapter.setList(bankChapterBean.getData());
                            else
                                mChaptersAdapter.addToList(bankChapterBean.getData());

                            if (bankChapterBean.get_metadata().getRemaining_pages() > currentPage) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
//                            binding.subjectLectureQbank.setText(bankChapterBean.getData().size()+" Chapters");
                            hideEmptyView();
                            insertChaptersInDatabase(bankChapterBean);
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideAvlIndicator();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetQuestionBankChapterBean> call, Throwable t) {
                hideAvlIndicator();
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    showEmptyView();
                }
            }
        });
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void insertChaptersInDatabase(GetQuestionBankChapterBean bankChapterBean) {
        if (bankChapterBean.getData() != null)
            mDBRepositoryKotlin.insertSubjectChapters(bankChapterBean.getData());
    }

    private void getSubjectChaptersFromRoom(String question_category) {
        mDBRepository.getSubjectChapters(question_category).observe(this, list -> {
            if (list.size() > 0) {
                if (mChaptersAdapter.getList().size() == 0) {
                    mChaptersAdapter.setList(list);
                    hideEmptyView();
                    hideAvlIndicator();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void getFreeChaptersFromRoom() {
        mDBRepository.getFreeChapters().observe(this, (List<ChapterBean> list) -> {
            if (list.size() > 0) {
                if (mChaptersAdapter.getList().size() == 0) {
                    mChaptersAdapter.setList(list);
                    hideEmptyView();
                    hideBaseProgress();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerFreeVideosQBank.setVisibility(View.GONE);
        binding.recyclerFreeVideosQBank.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerFreeVideosQBank.setVisibility(View.VISIBLE);
        binding.linearEmptyViewQBank.setVisibility(View.GONE);
    }

    private void downloadChapterQuestions(ChapterBean chapterBean) {
        showBaseProgress();
        mCallGetQuestions = AppController.getInstance()
                .getApis().getChapterQuestions(getHeader(), "1", chapterBean.getTitle(), "Paid");
        mCallGetQuestions.enqueue(new ResponseHandler<GetCustomModuleQuestionsBean>() {
            @Override
            public void onSuccess(GetCustomModuleQuestionsBean getChapterQuestionsBean) {
                if (getChapterQuestionsBean != null) {
                    if (getChapterQuestionsBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (getChapterQuestionsBean.getData().size() > 0) {
                            setDownloaded(chapterBean, "Yes");
                            insertQuestion(chapterBean, getChapterQuestionsBean);
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                //if (t != null) showErrorToast(t.getMessage());
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

    private void insertQuestion(ChapterBean chapterBean, GetCustomModuleQuestionsBean model) {
        for (int i = 0; i < model.getData().size(); i++) {
            model.getData().get(i).setDownloaded(true);
            model.getData().get(i).setChapterId(chapterBean.getId());
        }
        if (model.getData() != null) mDBRepositoryKotlin.insertQuestions(model.getData());
        showToast("Downloaded Successfully");
        markChapterDownloaded(chapterBean);
    }

    private void showAvlIndicator() {
        binding.progressQBank.setVisibility(View.VISIBLE);
        binding.avlQBank.show();
    }

    private void hideAvlIndicator() {
        binding.progressQBank.setVisibility(View.GONE);
        binding.avlQBank.hide();
    }



}