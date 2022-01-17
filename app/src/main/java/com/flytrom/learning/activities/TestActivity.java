package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.SignUpActivity;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.activities.q_bank_menu.TestsActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.ActivityTestBinding;
import com.flytrom.learning.databinding.ActivityTestsBinding;
import com.flytrom.learning.databinding.ItemTestMenuSubjectBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class TestActivity extends BaseActivity<ActivityTestBinding> implements VerticalPagination.VerticalScrollListener{
    private SimpleRecyclerViewAdapter<SubjectBean, ItemTestMenuSubjectBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetSubjects;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    String catogeryId,subject,image,name;
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        getSubjects(mCurrentPage, false);
        Intent intent1 = getIntent();
        catogeryId = intent1.getStringExtra("CategoryId");

//        binding.toolbar.textTitle.setText(mSubjectBean.getTitle());
        subject = intent1.getStringExtra("Subject");
        image = intent1.getStringExtra("image");
        name = intent1.getStringExtra("name");
        if (subject.equalsIgnoreCase("1")){
            binding.subjectLecture.setText(subject+" Subject");
        }else {
            binding.subjectLecture.setText(subject+" Subjects");
        }
        binding.lectureName.setText(name);
        Glide.with(this).load(Constants.MEDIA_URL + image).into(binding.lectureImg);
        Log.d("QWERTYUIO","ID "+catogeryId +","+"subject "+subject+","+"image "+Constants.MEDIA_URL + image);
        initView();

    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }
    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.card_back_test:
               goBack();
               break;

        }

    };

    @Override
    protected int getContentView() {
        return R.layout.activity_test;
    }

    @Override
    public void onLoadMore() {
        getSubjects(mCurrentPage, false);
    }

    private void initView() {
        setBaseCallback(baseCallback);
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.testt_item_list, BR.bean, (v, subjectBean) -> {

            Bundle bundle = new Bundle();
            if (subjectBean.getType() != null) {
                bundle.putString("type", Constants.FREE);
            } else {
                bundle.putSerializable("chapterBean", subjectBean);
                bundle.putString("type", Constants.PAID);
            }

            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);

            bundle.putString("from", "testMenu");
            Objects.requireNonNull(this).startActivity(new Intent(TestActivity.this, TestsActivity.class).putExtras(bundle).putExtra("image",image));
            goNextAnimation();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TestActivity.this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerSubjects.setLayoutManager(linearLayoutManager);
        binding.recyclerSubjects.addOnScrollListener(mVerticalPagination);
        binding.recyclerSubjects.setAdapter(mSubjectsAdapter);

        getSubjectsFromRoom();
        if (AppController.getInstance().isInternetOn()) getSubjects(mCurrentPage, false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    private void getSubjects(int currentPage, boolean progress) {
        showBaseProgress();
        mCallGetSubjects = AppController.getInstance().getApis().getSubjects2(getHeader(),String.valueOf(currentPage), "test", "tests_visibility",catogeryId);
        mCallGetSubjects.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {
                hideBaseProgress();

                if (getSubjectsBean != null) {
                    mSubjectsAdapter.clearList();
                    if (getSubjectsBean.getStatus() == Constants.SUCCESS_CODE) {
                        binding.rlTestWhite.setVisibility(View.GONE);
                        if (getSubjectsBean.getData() != null && getSubjectsBean.getData().size() > 0) {
                            if (mCurrentPage == 1) {
                                prepareDefaultListAndSet(getSubjectsBean.getData());
                            } else {
                                mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            }
//                            for (int i = 0; i < getSubjectsBean.getData().size(); i++) {
//                                getSubjectsBean.getData().get(i).setTitle_category(name);
//                                if (getSubjectsBean.getData().get(i).getTotalTests().equalsIgnoreCase("0")){
//
//                                }else {
//                                    int totalQBank = Integer.parseInt(getSubjectsBean.getData().get(i).getTotalTests());
//                                    int totalQBankComplete = Integer.parseInt(getSubjectsBean.getData().get(i).getTotalTestCompleted());
//                                    int res = totalQBank * 100 / totalQBankComplete;
//                                    getSubjectsBean.getData().get(i).setPercentage_test(String.valueOf(res));
//                                    Log.d("totalQBank", String.valueOf(res));
//                                }
//
//                            }

//                                mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            if (mCurrentPage < getSubjectsBean.getMetadata().getRemainingPages()) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }

                            insertSubjectsInDatabase(getSubjectsBean);


                        }
                    }

                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                // if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetSubjectsBean> call, Throwable t) {
                hideBaseProgress();
                // onCallComplete(call, t);
                //if (t != null) getSubjectsFromRoom();
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                hideBaseProgress();
                if (t == Constants.NO_DATA) {
                    if (mCurrentPage == 1) {
                        hideEmptyView();
                    }
                }
            }
        });
    }

    private void prepareDefaultListAndSet(List<SubjectBean> list) {
        mSubjectsAdapter.setList(list);
    }

    private void insertSubjectsInDatabase(GetSubjectsBean model) {
        if (model.getData() != null) {
            for (int i = 0; i < model.getData().size(); i++) {
                model.getData().get(i).setOrderType("test");
            }
            mDBRepositoryKotlin.insertSubjects(model.getData());
            hideEmptyView();
        }
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("test").observe(this, list -> {
            if (list.size() > 0) {
                if (mSubjectsAdapter.getList().size() == 0) {
                    mSubjectsAdapter.setList(list);
                    hideEmptyView();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerSubjects.setVisibility(View.GONE);
        binding.linearEmptyViewTest.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerSubjects.setVisibility(View.VISIBLE);
        binding.linearEmptyViewTest.setVisibility(View.GONE);
    }
}