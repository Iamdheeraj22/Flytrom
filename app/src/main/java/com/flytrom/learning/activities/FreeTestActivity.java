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
import com.flytrom.learning.activities.q_bank_menu.TestsActivity;
import com.flytrom.learning.activities.test_menu.GoingToStartTestActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleQuestionsBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.test_bean.GetTestsBean;
import com.flytrom.learning.beans.response_beans.test_bean.TestBean;
import com.flytrom.learning.databinding.ActivityFreeTestBinding;
import com.flytrom.learning.databinding.ActivityTestsBinding;
import com.flytrom.learning.databinding.DialogConfirmBinding;
import com.flytrom.learning.databinding.ItemTestBinding;
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

public class FreeTestActivity extends BaseActivity<ActivityFreeTestBinding>
        implements VerticalPagination.VerticalScrollListener{
    private SimpleRecyclerViewAdapter<TestBean, ItemTestBinding> mParticularSubTestsAdapter;
    private SubjectBean mSubjectBean;
    private Call<GetTestsBean> mCallTests;
    private VerticalPagination mVerticalPagination;
    private int mCurrentPage = 1;
    private String mFrom, mTestType;
    private Call<GetCustomModuleQuestionsBean> mCallGetQuestions;
    private Call<SuccessBean> mCallMarkDownload;
    private List<SolveMCQBean> solveMCQList;
    private BaseCustomDialog<DialogConfirmBinding> mConfirmDialog;
    String image;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Intent intent1 = getIntent();
        binding.toolbar.textTitle.setText("Free Test");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_free_test;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }



    @Override
    public void onDestroy() {
        if (mCallTests != null) mCallTests.cancel();
        if (mCallGetQuestions != null) mCallGetQuestions.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }


    @Override
    public void onLoadMore() {
        getTests(mCurrentPage, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.TEST_ITEM) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        int position = bundle.getInt("position");
                        TestBean testBean = mParticularSubTestsAdapter.getList().get(position);
                        if (bundle.getBoolean("reset")) {
                            testBean.setSavedData(null);
                            testBean.setIsCompleted("0");
                        }

                        solveMCQList = (List<SolveMCQBean>) bundle.getSerializable("solveMCQList");
                        if (solveMCQList != null && solveMCQList.size() > 0) {
                            testBean.setSavedData(solveMCQList);
                            testBean.setIsCompleted("0");
                            testBean.setStatus(Constants.CONTENT_STATUS[1]);
                        }

                        mParticularSubTestsAdapter.getList().set(position, testBean);
                        mParticularSubTestsAdapter.notifyItemChanged(position);
                    }
                }
            }
        }
    }

    private void initView() {
        solveMCQList = new ArrayList<>();
        mParticularSubTestsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.test_play_list, BR.bean,
                new SimpleRecyclerViewAdapter.SimpleCallback<TestBean>() {
            @Override
            public void onClick(View v, TestBean testBean) {

            }

            @Override
            public void onClickWithPosition(View v, TestBean testBean, int pos) {
                switch (v.getId()) {
                    case R.id.btn_download:
                        testBean.setPosition(pos);
                        downloadTest(testBean);
                        break;
                    case R.id.relative_test:
                        goToGoingToStartTest(testBean, pos);
                        break;
                }
//                if (PrefUtils.getInstance().getSelectedSubject() != null)
//                    showPurchasePlanDialog(getString(R.string.test));
//                else {
//
//                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerFreeVideosTest.setLayoutManager(linearLayoutManager);
        binding.recyclerFreeVideosTest.addOnScrollListener(mVerticalPagination);
        binding.recyclerFreeVideosTest.setAdapter(mParticularSubTestsAdapter);

        setBaseCallback(baseCallback);
        getData();
    }
    private void goToGoingToStartTest(TestBean testBean, int pos) {
        testBean.setPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("testBean", testBean);
        bundle.putString("from", mFrom);
        startActivityForResult(new Intent(FreeTestActivity.this, GoingToStartTestActivity.class)
                .putExtras(bundle), Constants.TEST_ITEM);
        goNextAnimation();
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mSubjectBean = (SubjectBean) bundle.getSerializable("chapterBean");
            if (mSubjectBean != null) {
//                binding.lectureNameTest.setText(mSubjectBean.getTitle());
//                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
            mFrom = bundle.getString("from");
            mTestType = bundle.getString("type");

            if (mTestType != null) {
//                if (mTestType.equals("Free"))
//                    binding.lectureName.setText(getString(R.string.text_free_tests));
//                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
            }
        }

        if (mSubjectBean != null) {
            getSubjectTestsFromRoom(mSubjectBean.getTitle());
        } else {
            getFreeTestsFromRoom();
        }
        if (AppController.getInstance().isInternetOn()) getTests(mCurrentPage, true);

    }

    private void downloadTest(TestBean testBean) {
        if (AppController.getInstance().isInternetOn()) {
            if (testBean.getIs_downloaded().equals("No")) {
                downloadTestQuestions(testBean);
            } else {
                deleteDownloadedTestDialog(testBean);
            }
        } else
            showToast(getString(R.string.no_internet));
    }

    private void deleteDownloadedTestDialog(TestBean testBean) {
        mConfirmDialog = new BaseCustomDialog<>(this, R.layout.dialog_confirm, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmDialog.dismiss();
                    break;
                case R.id.text_yes:
                    mConfirmDialog.dismiss();
                    setDownloaded(testBean, "No");
                    markTestDownload(testBean);
                    break;
            }
        });
        Objects.requireNonNull(mConfirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mConfirmDialog.getBinding().textMessage.setText(R.string.title_remove_downloaded_test);
        mConfirmDialog.getBinding().imageIcon.setImageResource(R.drawable.ic_alert);
        mConfirmDialog.getBinding().textYes.setText(R.string.text_yes);
        mConfirmDialog.setCancelable(true);
        mConfirmDialog.show();
    }

    private void downloadTestQuestions(TestBean testBean) {
        showBaseProgress();
        mCallGetQuestions = AppController.getInstance()
                .getApis().getTestQuestions(getHeader(), "1", testBean.getId());
        mCallGetQuestions.enqueue(new ResponseHandler<GetCustomModuleQuestionsBean>() {
            @Override
            public void onSuccess(GetCustomModuleQuestionsBean getTestQuestionsBean) {
                if (getTestQuestionsBean != null) {
                    if (getTestQuestionsBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (getTestQuestionsBean.getData().size() > 0) {
                            setDownloaded(testBean, "Yes");
                            insertQuestion(testBean, getTestQuestionsBean);
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

    private void setDownloaded(TestBean testBean, String type) {
        mParticularSubTestsAdapter.getList().get(testBean.getPosition()).setIs_downloaded(type);
        mParticularSubTestsAdapter.notifyItemChanged(testBean.getPosition());

        testBean.setIs_downloaded(type);
        mDBRepositoryKotlin.updateDownloadStatusOfTest(testBean);
    }

    private void insertQuestion(TestBean testBean, GetCustomModuleQuestionsBean model) {
        for (int i = 0; i < model.getData().size(); i++) {
            model.getData().get(i).setDownloaded(true);
            model.getData().get(i).setTestId(testBean.getId());
        }
        if (model.getData() != null) mDBRepositoryKotlin.insertQuestions(model.getData());
        showToast("Downloaded Successfully");
        markTestDownload(testBean);
    }

    private void markTestDownload(TestBean testBean) {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(testBean.getId()));
        map.put("type", "tests");
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

    private void getTests(int currentPage, boolean progress) {
        if (progress) showAvlIndicator();
            mCallTests = AppController.getInstance().getApis().getFreeTests(getHeader(),
                    String.valueOf(currentPage),  "Free");
        mCallTests.enqueue(new ResponseHandler<GetTestsBean>() {
            @Override
            public void onSuccess(GetTestsBean getTestBean) {

                if (getTestBean != null) {
                    if (getTestBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getTestBean.getData() != null && getTestBean.getData().size() > 0) {
                            /*for (int i = 0; i < getTestBean.getData().size(); i++) {
                                getTestBean.getData().get(i).setTitle_category(mSubjectBean.getTitle_category());}
*/
                            for (int i = 0; i < getTestBean.getData().size(); i++) {
                                if (getTestBean.getData().get(i).getTotalQuestions().equalsIgnoreCase("0")){
                                    getTestBean.getData().get(i).setProgress(String.valueOf("0"));
                                }else {
                                    if (getTestBean.getData().get(i).getSavedData().size() != 0){
                                        float totalQBank = Integer.parseInt(getTestBean.getData().get(i).getTotalQuestions());
                                        float totalQBankComplete = getTestBean.getData().get(i).getSavedData().size();
                                        float res = (totalQBankComplete / totalQBank) * 100;
                                        int total = Math.round(res);
                                        Log.d("totalQBank", String.valueOf(total));
                                        getTestBean.getData().get(i).setProgress(String.valueOf(total));
                                    }else {
                                        getTestBean.getData().get(i).setProgress(String.valueOf("0"));
                                    }

                                }

                            }

                            if (mCurrentPage == 1)
                                mParticularSubTestsAdapter.setList(getTestBean.getData());
                            else
                                mParticularSubTestsAdapter.addToList(getTestBean.getData());

                            if (getTestBean.getMetadata().getRemainingPages() > currentPage) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
                            hideEmptyView();
                            insertTests(getTestBean);
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
            public void onComplete(Call<GetTestsBean> call, Throwable t) {
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
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
        }
    };

    private void insertTests(GetTestsBean model) {
        if (model.getData() != null) mDBRepositoryKotlin.insertTests(model.getData());
    }

    private void getSubjectTestsFromRoom(String subName) {
        mDBRepository.getSubjectTests(subName).observe(this, (List<TestBean> list) -> {
            if (list.size() > 0) {
                if (mParticularSubTestsAdapter.getList().size() == 0) {
                    mParticularSubTestsAdapter.setList(list);
                    hideEmptyView();
                    hideAvlIndicator();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void getFreeTestsFromRoom() {
        mDBRepository.getFreeTests(mTestType).observe(this, (List<TestBean> list) -> {
            if (list.size() > 0) {
                if (mParticularSubTestsAdapter.getList().size() == 0) {
                    mParticularSubTestsAdapter.setList(list);
                    hideEmptyView();
                    hideBaseProgress();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerFreeVideosTest.setVisibility(View.GONE);
        binding.recyclerFreeVideosTest.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerFreeVideosTest.setVisibility(View.VISIBLE);
        binding.linearEmptyViewTest.setVisibility(View.GONE);
    }

    private void showAvlIndicator() {
        binding.progressTest.setVisibility(View.VISIBLE);
        binding.avlTest.show();
    }

    private void hideAvlIndicator() {
        binding.progressTest.setVisibility(View.GONE);
        binding.avlTest.hide();
    }
}