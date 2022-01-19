package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.video_menu.FreeVideosActivity;
import com.flytrom.learning.activities.video_menu.SavedVideosActivity;
import com.flytrom.learning.activities.video_menu.SubjectVideosActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.subject.GetSubjectsBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivityLecturesBinding;
import com.flytrom.learning.databinding.ItemVideosMenuBinding;
import com.flytrom.learning.databinding.VideoItemListBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.List;

import retrofit2.Call;

public class LecturesActivity extends BaseActivity<ActivityLecturesBinding> implements VerticalPagination.VerticalScrollListener {
    private SimpleRecyclerViewAdapter<SubjectBean, VideoItemListBinding> mSubjectsAdapter;
    private Call<GetSubjectsBean> mCallGetSubjects;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
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
        binding.lectureName.setText(name);
        Glide.with(this).load(Constants.MEDIA_URL + image).into(binding.lectureImg);
        Log.d("QWERTYUIO","ID "+catogeryId +","+"subject "+subject+","+"image "+Constants.MEDIA_URL + image);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_lectures;
    }

    @Override
    public void onLoadMore() {
        getSubjects(mCurrentPage, false);
    }

    private void initView() {
        mSubjectsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.video_item_list, BR.bean, (v, subjectBean) -> {

            if (subjectBean.isLocked())
                PrefUtils.getInstance().setSelectedSubject(subjectBean.getTitle());
            else
                PrefUtils.getInstance().setSelectedSubject(null);

            switch (subjectBean.getId()) {
                case Constants.SAVED_VIDEOS_MENU_ID:
                    startActivity(new Intent(this, SavedVideosActivity.class));
                    break;
                case Constants.FREE_VIDEOS_MENU_ID:
                    startActivity(new Intent(this, FreeVideosActivity.class));
                    break;
                default:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("subjectBean", subjectBean);
                    startActivity(new Intent(this, SubjectVideosActivity.class).putExtras(bundle).putExtra("image",image));
                    break;
            }
            goNextAnimation();
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        mVerticalPagination = new VerticalPagination(gridLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerSubjects.setLayoutManager(gridLayoutManager);
        binding.recyclerSubjects.addOnScrollListener(mVerticalPagination);
        binding.recyclerSubjects.setAdapter(mSubjectsAdapter);
        setBaseCallback(baseCallback);
        //getSubjectsFromRoom();
        if (AppController.getInstance().isInternetOn()) {getSubjects(mCurrentPage, false);}
    }

    private void getSubjects(int currentPage, boolean progress) {
        showBaseProgress();

        mCallGetSubjects = AppController.getInstance().getApis().getSubjects2(getHeader(), String.valueOf(currentPage), "video", "videos_visibility",catogeryId);
        mCallGetSubjects.enqueue(new ResponseHandler<GetSubjectsBean>() {
            @Override
            public void onSuccess(GetSubjectsBean getSubjectsBean) {

//                for (int i = 0; i < getSubjectsBean.getData().size(); i++) {
//                    if (getSubjectsBean.getData().get(i).getTotalVideos().equalsIgnoreCase("0")){
//                    }else {
//                        int totalVideo = Integer.parseInt(getSubjectsBean.getData().get(i).getTotalVideos());
//                        int totalVideoComplete = Integer.parseInt(getSubjectsBean.getData().get(i).getTotalVideoCompleted());
//
//                        int res = totalVideo * 100 / totalVideoComplete;
//                        getSubjectsBean.getData().get(i).setPercentage(String.valueOf(res));
//
//                    }
//                }
                binding.rlWhite.setVisibility(View.GONE);
                if (getSubjectsBean != null) {
                    if (getSubjectsBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (getSubjectsBean.getData() != null && getSubjectsBean.getData().size() > 0) {
                            if (mCurrentPage == 1) {
                                prepareDefaultListAndSet(getSubjectsBean.getData());
                            }
                            else {
                                mSubjectsAdapter.addToList(getSubjectsBean.getData());
                            }
                            for (int i = 0; i < getSubjectsBean.getData().size(); i++) {
                                getSubjectsBean.getData().get(i).setTitle_category(name);
                            }

                            if (mCurrentPage < getSubjectsBean.getMetadata().getRemainingPages()) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
                            insertSubjectsInDatabase(getSubjectsBean.getData());
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                Log.i("subjectError",t.getMessage());
                showToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetSubjectsBean> call, Throwable t) {
                hideBaseProgress();
                if(t!=null){
                    showToast(t.getLocalizedMessage());
                }

                //onCallComplete(call, t);
                //if (t != null) getSubjectsFromRoom();
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
    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.card_back_lecture:
                goBack();
                break;

        }
    };

    private void prepareDefaultListAndSet(List<SubjectBean> list) {
        mSubjectsAdapter.setList(list);
    }
    private void insertSubjectsInDatabase(List<SubjectBean> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setOrderType("video");
            }
            mDBRepositoryKotlin.insertSubjects(list);
            hideEmptyView();
        }
    }

    private void getSubjectsFromRoom() {
        mDBRepository.getSubjects("video").observe(this, list -> {
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
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerSubjects.setVisibility(View.VISIBLE);
        binding.emptyView.setVisibility(View.GONE);
    }

}