package com.flytrom.learning.activities.video_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.videos.GetVideosBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivitySubjectVideosBinding;
import com.flytrom.learning.databinding.ItemParticularSubjectsVideoBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import retrofit2.Call;

public class SubjectVideosActivity extends BaseActivity<ActivitySubjectVideosBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<VideoBean, ItemParticularSubjectsVideoBinding> mParticularSubVideosAdapter;
    private SubjectBean mSubjectBean;
    private Call<GetVideosBean> mCallGetVideos;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;
    String image;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Intent intent1 = getIntent();
        image = intent1.getStringExtra("image");
        Glide.with(this).load(Constants.MEDIA_URL + image).into(binding.lectureImgVideo);
        Log.d("QWERTYUIO","image "+Constants.MEDIA_URL + image);
        getData();
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_subject_videos;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onDestroy() {
        if (mCallGetVideos != null) mCallGetVideos.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLoadMore() {
        getVideos(mCurrentPage, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.VIDEO_ITEM) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        VideoBean videoBean = (VideoBean) bundle.getSerializable("videoBean");
                        if (videoBean != null) { if (mParticularSubVideosAdapter.getList().size() > 0) {
                                mParticularSubVideosAdapter.getList().set(videoBean.getPosition(), videoBean);
                                mParticularSubVideosAdapter.notifyItemChanged(videoBean.getPosition());
                            }
                        }
                    }
                }
            }
        }
    }

    private void initView() {
        mParticularSubVideosAdapter = new SimpleRecyclerViewAdapter<>(R.layout.video_play_list, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<VideoBean>() {

            @Override
            public void onClick(View v, VideoBean videoBean) {

            }

            @Override
            public void onClickWithPosition(View v, VideoBean videoBean, int pos) {
                if (videoBean.getType().equals(Constants.FREE)) {
                    goToPlayVideo(videoBean, pos);
                } else {
                    if (PrefUtils.getInstance().getSelectedSubject() != null)
                        showPurchasePlanDialog(getString(R.string.video));
//                        goToPlayVideo(videoBean, pos);
                    else
                        goToPlayVideo(videoBean, pos);
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerVideos.setLayoutManager(linearLayoutManager);
        binding.recyclerVideos.addOnScrollListener(mVerticalPagination);
        binding.recyclerVideos.setAdapter(mParticularSubVideosAdapter);

        setBaseCallback(baseCallback);

    }

    private void goToPlayVideo(VideoBean videoBean, int pos) {
        videoBean.setPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBean", videoBean);
        bundle.putString("videoId", String.valueOf(videoBean.getId()));
        startActivityForResult(new Intent(this, PlayVideoActivity.class).putExtras(bundle), Constants.VIDEO_ITEM);
        goNextAnimation();
    }

    private void getData() {
        if (getIntent().getExtras() != null) {
            mSubjectBean = (SubjectBean) getIntent().getSerializableExtra("subjectBean");
            if (mSubjectBean != null) {
//                binding.toolbar.textTitle.setText(mSubjectBean.getTitle());
                binding.lectureNameVideo.setText(mSubjectBean.getTitle());
//                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                getVideosFromRoom(mSubjectBean.getTitle());
                Log.d("Prsnt",mSubjectBean.getPercentage());
                if (AppController.getInstance().isInternetOn()) getVideos(mCurrentPage, true);
            }
        }
    }

    private void getVideos(int currentPage, boolean progress) {
        if (progress) showAvlIndicator();
        mCallGetVideos = AppController.getInstance().getApis().getVideos(getHeader(), String.valueOf(currentPage), mSubjectBean.getTitle(), "0");
        mCallGetVideos.enqueue(new ResponseHandler<GetVideosBean>() {
            @Override
            public void onSuccess(GetVideosBean getVideosBean) {
                Log.d("HGHJH", String.valueOf(getVideosBean.getData()));
                if (getVideosBean != null) {

                    if (getVideosBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getVideosBean.getData() != null && getVideosBean.getData().size() > 0) {
                            for (int i = 0; i < getVideosBean.getData().size(); i++) {
                                getVideosBean.getData().get(i).setTitle_category(mSubjectBean.getTitle());}

                            if (mCurrentPage == 1)
                                mParticularSubVideosAdapter.setList(getVideosBean.getData());
                            else
                                mParticularSubVideosAdapter.addToList(getVideosBean.getData());

                            if (getVideosBean.getMetadata().getRemainingPages() > currentPage) {
                                mCurrentPage++;
                                mVerticalPagination.setLoading(false);
                            }
                            if (getVideosBean.getData().size() == 1 || getVideosBean.getData().size() == 0){
                                binding.subjectLectureVideo.setText(getVideosBean.getData().size()+" Video");

                            }else {
                                binding.subjectLectureVideo.setText(getVideosBean.getData().size()+" Videos");
                            }
                            hideEmptyView();
                            insertVideos(getVideosBean);
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideAvlIndicator();
                //if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetVideosBean> call, Throwable t) {
                hideAvlIndicator();
                //onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    if (mCurrentPage == 1) {
                        showEmptyView();
                    }
                }
            }
        });
    }

    private final BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.card_back_lectureVideo:
                goBack();
                break;

        }
    };

    private void showEmptyView() {
        binding.recyclerVideos.setVisibility(View.GONE);
        binding
                .linearEmptyViewVideo.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerVideos.setVisibility(View.VISIBLE);
        binding.linearEmptyViewVideo.setVisibility(View.GONE);
    }

    private void insertVideos(GetVideosBean model) {
        if (model.getData() != null) mDBRepositoryKotlin.insertVideos(model.getData());
    }

    private void getVideosFromRoom(String videoCategory) {
        mDBRepository.getSubjectVideos(videoCategory).observe(this, list -> {
            if (list.size() > 0) {
                if (mParticularSubVideosAdapter.getList().size() == 0) {
                    mParticularSubVideosAdapter.setList(list);
                    hideAvlIndicator();
                    hideEmptyView();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showAvlIndicator() {
        binding.progressVideo.setVisibility(View.VISIBLE);
        binding.avlVideo.show();
    }

    private void hideAvlIndicator() {
        binding.progressVideo.setVisibility(View.GONE);
        binding.avlVideo.hide();
    }
}