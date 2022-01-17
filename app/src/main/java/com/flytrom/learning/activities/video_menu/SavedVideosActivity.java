package com.flytrom.learning.activities.video_menu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.databinding.ActivitySavedVideosBinding;
import com.flytrom.learning.databinding.ItemParticularSubjectsVideoBinding;
import com.flytrom.learning.utils.Constants;

public class SavedVideosActivity extends BaseActivity<ActivitySavedVideosBinding> {

    private SimpleRecyclerViewAdapter<VideoBean, ItemParticularSubjectsVideoBinding> mParticularSubVideosAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_saved_videos;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        binding.toolbar.textTitle.setText(getString(R.string.text_saved_lectures));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);

        mParticularSubVideosAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_particular_subjects_video, BR.bean, new SimpleRecyclerViewAdapter.SimpleCallback<VideoBean>() {
            @Override
            public void onClick(View v, VideoBean videoBean) {

            }

            @Override
            public void onClickWithPosition(View v, VideoBean videoBean, int pos) {
                goToPlayVideo(videoBean, pos);
            }
        });

        binding.recyclerVideos.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerVideos.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerVideos.setAdapter(mParticularSubVideosAdapter);

        getSavedVideosFromRoom();
    }

    private void goToPlayVideo(VideoBean videoBean, int pos) {
        videoBean.setPosition(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("videoBean", videoBean);
        startActivity(new Intent(SavedVideosActivity.this, PlayVideoActivity.class).putExtras(bundle));
        goNextAnimation();
    }

    private BaseCallback baseCallback = view -> {

        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void getSavedVideosFromRoom() {
        mDBRepository.getVideos(Constants.SAVED).observe(this, list -> {
            if (list.size() > 0) {
                if (mParticularSubVideosAdapter.getList().size() == 0) {
                    mParticularSubVideosAdapter.setList(list);
                    hideEmptyView();
                }
            } else {
                showEmptyView();
            }
        });
    }

    private void showEmptyView() {
        binding.recyclerVideos.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        binding.recyclerVideos.setVisibility(View.VISIBLE);
        binding.linearEmptyView.setVisibility(View.GONE);
    }
}
