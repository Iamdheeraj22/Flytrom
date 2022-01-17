package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.q_bank_menu.BookMarkedActivity;
import com.flytrom.learning.activities.q_bank_menu.ChaptersActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.custom_module.GetCustomModuleBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.databinding.ActivityLecturesBinding;
import com.flytrom.learning.databinding.ActivityQbankBinding;
import com.flytrom.learning.databinding.ItemQBankSubjectBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;

import retrofit2.Call;

public class QBankActivity extends BaseActivity<ActivityQbankBinding> implements VerticalPagination.VerticalScrollListener{
    private SimpleRecyclerViewAdapter<SubjectBean, ItemQBankSubjectBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetChapters;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    private Call<GetCustomModuleBean> mCallGetCustomModule;
    private boolean mIsCustomModuleExist;
    String catogeryId,subject,image,name;

    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Intent intent1 = getIntent();
        catogeryId = intent1.getStringExtra("CategoryId");
        subject = intent1.getStringExtra("Subject");
        image = intent1.getStringExtra("image");
        name = intent1.getStringExtra("name");
        if (subject.equalsIgnoreCase("1")){
            binding.subjectLecture.setText(subject+" Subject");
        }else {
            binding.subjectLecture.setText(subject+" Subjects");
        }

        getSubjects(mCurrentPage, false);
        binding.lectureName.setText(name);
        Glide.with(this).load(Constants.MEDIA_URL + image).into(binding.lectureImg);
        Log.d("QWERTYUIO","ID "+catogeryId +","+"subject "+subject+","+"image "+Constants.MEDIA_URL + image);
        initView();

    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_qbank;
    }

    @Override
    public void onLoadMore() {
        getSubjects(mCurrentPage, false);
    }

    private void initView() {
        setBaseCallback(callback);
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.q_bank_item_list, BR.bean, (v, subjectBean) -> {
            Bundle bundle = new Bundle();
            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);
            if (subjectBean.getId() == Constants.FREE_QUESTIONS_MENU_ID) {
                bundle.putString("type", Constants.FREE);
            } else {
                bundle.putString("type", Constants.PAID);
                bundle.putSerializable("subjectBean", subjectBean);
            }

            /*
            *
            *
            *
            * */

            startActivity(new Intent(this, ChaptersActivity.class).putExtras(bundle).putExtra("image",image));
            goNextAnimation();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerQBank.setLayoutManager(linearLayoutManager);
        binding.recyclerQBank.addOnScrollListener(mVerticalPagination);
        binding.recyclerQBank.setAdapter(mSubjectsAdapter);
        setBaseCallback(baseCallback);
        getSubjectsFromRoom();

        if (AppController.getInstance().isInternetOn()) getSubjects(mCurrentPage, false);

        getCustomModuleFromRoom();
        if (AppController.getInstance().isInternetOn()) getCustomModule();
    }
    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.card_bookmark:
                goToBookMarkScreen();
                break;
            case R.id.card_custom_module:
                if (mIsCustomModuleExist) {
                    startActivity(new Intent(this, GoingToStartModuleActivity.class));
                } else {
                    startActivity(new Intent(this, CreateCustomModuleActivity.class));
                }
                goNextAnimation();
                break;
        }
    };
    private void goToBookMarkScreen() {
        startActivity(new Intent(this, BookMarkedActivity.class).putExtra("from", "bookmark"));
        goNextAnimation();
    }

    private void getCustomModule() {
        mCallGetCustomModule = AppController.getInstance().getApis().getCustomModule(getHeader());
        mCallGetCustomModule.enqueue(new ResponseHandler<GetCustomModuleBean>() {
            @Override
            public void onSuccess(GetCustomModuleBean getCustomModuleBean) {

                if (getCustomModuleBean != null) {
                    if (getCustomModuleBean.getStatus() == Constants.SUCCESS_CODE) {
                        insertCustomModuleInDatabase(getCustomModuleBean);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetCustomModuleBean> call, Throwable t) {
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getSubjects(int currentPage, boolean progress) {
        //if (progress) showBaseProgress();
        mCallGetChapters = AppController.getInstance().getApis().getSubjects2(getHeader(), String.valueOf(currentPage), "qb", "question_bank_visibility",catogeryId);
        mCallGetChapters.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {

                if (getSubjectsBean != null) {
                    if (getSubjectsBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getSubjectsBean.getData() != null && getSubjectsBean.getData().size() > 0) {
                            binding.rlWhiteBack.setVisibility(View.GONE);
                            prepareDefaultListAndSet(getSubjectsBean.getData());
                            if (mCurrentPage == 1) {
                                prepareDefaultListAndSet(getSubjectsBean.getData());
                            } else {
                                mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            }

                            for (int i = 0; i < getSubjectsBean.getData().size(); i++) {
                                getSubjectsBean.getData().get(i).setTitle_category(name);
                            }
                            mSubjectsAdapter.clearList();
                            mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            if (mCurrentPage < getSubjectsBean.getMetadata().getRemainingPages()) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
                            setBookmarkLocally(getSubjectsBean.getBookmarkCounter());
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
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
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

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.card_back_qbank:
                goBack();
                break;

        }
    };

    private void setBookmarkLocally(int bookmarkCounter) {
        PrefUtils.getInstance().setBookmarkCounter(bookmarkCounter);
//        binding.textBookmarkCount.setText(String.valueOf(bookmarkCounter));
    }

    private void insertSubjectsInDatabase(GetSubjectsBean model) {
        if (model.getData() != null) {
            for (int i = 0; i < model.getData().size(); i++) {
                model.getData().get(i).setOrderType("qb");
            }
            mDBRepositoryKotlin.insertSubjects(model.getData());
        }

        hideEmptyView();
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("qb").observe(this, list -> {
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

    private void insertCustomModuleInDatabase(GetCustomModuleBean model) {
        if (model.getData() != null) mDBRepositoryKotlin.insertCustomModule(model.getData());
    }

    private void getCustomModuleFromRoom() {
        mDBRepository.getCustomModuleFromRoom().observe(this, list -> {
            mIsCustomModuleExist = list.size() > 0;
        });
    }

    private void showEmptyView() {
        binding.recyclerQBank.setVisibility(View.GONE);
        binding.linearEmptyView2.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerQBank.setVisibility(View.VISIBLE);
        binding.linearEmptyView2.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubjects(mCurrentPage, false);
    }
}