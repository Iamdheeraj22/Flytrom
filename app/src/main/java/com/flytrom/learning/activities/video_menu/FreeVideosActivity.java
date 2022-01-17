package com.flytrom.learning.activities.video_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.videos.GetVideosBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivityFreeVideosBinding;
import com.flytrom.learning.databinding.ItemFreeVideosBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import timber.log.Timber;

public class FreeVideosActivity extends BaseActivity<ActivityFreeVideosBinding>
        implements VerticalPagination.VerticalScrollListener {

    private SimpleRecyclerViewAdapter<VideoBean, ItemFreeVideosBinding> mFreeVideosAdapter;
    private Call<GetVideosBean> mCallGetVideos;
    private int mCurrentPage = 1;
    private VerticalPagination mVerticalPagination;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_free_videos;
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
    public void onLoadMore() {
        getVideos(mCurrentPage, false);
    }


    private void initView() {
        binding.toolbar.textTitle.setText(getString(R.string.free_lectures));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));

        mFreeVideosAdapter = new SimpleRecyclerViewAdapter<>(R.layout.video_play_list, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<VideoBean>() {
            @Override
            public void onClick(View v, VideoBean videoBean) {

            }

            @Override
            public void onClickWithPosition(View v, VideoBean videoBean, int pos) {
                goToPlayVideo(videoBean, pos);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.recyclerFreeVideos.setLayoutManager(linearLayoutManager);
        binding.recyclerFreeVideos.addOnScrollListener(mVerticalPagination);
        binding.recyclerFreeVideos.setAdapter(mFreeVideosAdapter);

        setBaseCallback(baseCallback);

        getVideosFromRoom();
        if (AppController.getInstance().isInternetOn()) getVideos(mCurrentPage, true);
    }

    private void goToPlayVideo(VideoBean videoBean, int pos) {
        videoBean.setPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBean", videoBean);
        bundle.putString("videoId", String.valueOf(videoBean.getId()));
        startActivity(new Intent(this, PlayVideoActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void getVideos(int mCurrentPage, boolean progress) {
        if (progress) showAvlIndicator();
        mCallGetVideos = AppController.getInstance().getApis().getVideos(getHeader(),
                String.valueOf(mCurrentPage), null, "1");
        mCallGetVideos.enqueue(new ResponseHandler<GetVideosBean>() {
            @Override
            public void onSuccess(GetVideosBean getVideosBean) {

                if (getVideosBean != null) {
                    if (getVideosBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getVideosBean.getData() != null && getVideosBean.getData().size() > 0) {

                            prepareAndSetList(getVideosBean);
                            insertVideosInDatabase(getVideosBean.getData());
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

    private void prepareAndSetList(GetVideosBean list) {
        List<VideoBean> responseList = new ArrayList<>();
        for (int i = 0; i < list.getData().size(); i++) {
            if (list.getData().get(i).getType().equals("Free")) {
                responseList.add(list.getData().get(i));
            }
        }

        if (responseList.size() > 0) {
            if (mCurrentPage == 1)
                mFreeVideosAdapter.setList(responseList);
            else if (mCurrentPage < list.getMetadata().getRemainingPages()) {
                mCurrentPage++;
                mVerticalPagination.setLoading(false);
            }

            hideEmptyView();
        }
    }

    private void insertVideosInDatabase(List<VideoBean> list) {
        List<VideoBean> responseList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getType().equals("Free")) {
                responseList.add(list.get(i));
            }
        }
        mDBRepositoryKotlin.insertVideos(responseList);
    }

    private void getVideosFromRoom() {
        Timber.d("df");
        mDBRepository.getVideos(Constants.FREE).observe(this, list -> {
            if (list.size() > 0) {
                if (mFreeVideosAdapter.getList().size() == 0) {
                    mFreeVideosAdapter.setList(list);
                    hideEmptyView();
                    hideAvlIndicator();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerFreeVideos.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerFreeVideos.setVisibility(View.VISIBLE);
        binding.linearEmptyView.setVisibility(View.GONE);
    }

    private void showAvlIndicator() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.avl.show();
    }

    private void hideAvlIndicator() {
        binding.progress.setVisibility(View.GONE);
        binding.avl.hide();
    }
}
