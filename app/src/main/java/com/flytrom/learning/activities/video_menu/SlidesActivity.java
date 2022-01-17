package com.flytrom.learning.activities.video_menu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.SlideImageAdapter;
import com.flytrom.learning.adapters.VideoCategoryAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.beans.response_beans.others.SlidesBean;
import com.flytrom.learning.databinding.ActivitySlidesBinding;
import com.flytrom.learning.databinding.ItemImageSlidesBinding;
import com.flytrom.learning.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlidesActivity extends BaseActivity<ActivitySlidesBinding> {

    private SimpleRecyclerViewAdapter<SlidesBean, ItemImageSlidesBinding> mSlidesAdapter;
    private VideoBean videoBean;
    private SlideImageAdapter slideImageAdapter;
    int poss = 0;
    boolean open = false;
    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_slides;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        binding.toolbar.textTitle.setText("Slides");
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);

        mSlidesAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_image_slides, BR.bean, (v, slidesBean) -> {
//            if (binding.rlSlide.getVisibility() != View.VISIBLE) {
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            showSlideImage(slidesBean.getFile());
//            }
        });

        binding.recyclerSlides.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerSlides.setAdapter(mSlidesAdapter);


        getData();
    }

    private int getItem(int i) {
        return binding.viewPagerMain.getCurrentItem() + i;
    }

    private int getItem2(int i2) {
        return binding.viewPagerMain.getCurrentItem() - i2;
    }

    private void showSlideImage(String file) {

        binding.rlSlide.setVisibility(View.VISIBLE);
        open = true;
        for (int i = 0; i < videoBean.getSlides().size(); i++) {
            if (file.equalsIgnoreCase(videoBean.getSlides().get(i).getFile())){
                poss = i;
            }
        }
        callAdapter(videoBean.getSlides(), file);


    }

    private void callAdapter(List<SlidesBean> slides, String file) {
        slideImageAdapter = new SlideImageAdapter(slides, SlidesActivity.this, file);
        binding.viewPagerMain.setAdapter(slideImageAdapter);
        binding.viewPagerMain.setCurrentItem(poss);
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        final int newOrientation = newConfig.orientation;
        super.onConfigurationChanged(newConfig);
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            // hide other views

            binding.relativeParticularSlide.setVisibility(View.VISIBLE);
            binding.imageParticularSlide.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            binding.imageParticularSlide.setScaleType(ImageView.ScaleType.FIT_XY);
            Picasso.with(SlidesActivity.this).load(Constants.MEDIA_URL + slidesFile)
                    .placeholder(R.drawable.image_test_ques_p_holder)
                    .into(binding.imageParticularSlide);
            //showSystemUi(false);
        } else {
            // show other views
            binding.imageParticularSlide.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            //binding.imageParticularSlide.setAdjustViewBounds(true);
            // show system windows
            showSystemUi(true);
            binding.relativeParticularSlide.setVisibility(View.GONE);
        }
    }*/

    private void showSystemUi(boolean show) {
        //Log.v(TAG, (show ? "show" : "hide") + " system ui");
        if (!show) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.image_exit_full_screen:
                binding.relativeParticularSlide.setVisibility(View.GONE);
                /*                Log.d("dfd", "ddf");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);*/
                break;
            case R.id.image_exit_full_screen2:
                binding.rlSlide.setVisibility(View.GONE);
                poss = 0;
                open = false;
                break;
            case R.id.imgNextSlide:
                int current = getItem(+1);
                if (current < videoBean.getSlides().size()) {
                    binding.viewPagerMain.setCurrentItem(current);
                } else {

                }
                break;
            case R.id.imgBackSlide:
                int current2 = getItem2(1);
                if (current2 < 0) {

                } else {
                    binding.viewPagerMain.setCurrentItem(current2);
                }
                break;
        }
    };

    private void getData() {
        if (getIntent().getExtras() != null) {
            videoBean = (VideoBean) getIntent().getSerializableExtra("videoBean");
            if (videoBean != null) {
                //binding.toolbar.textTitle.setText(videoBean.getCategory());
                if (videoBean.getSlides() != null && videoBean.getSlides().size() > 0) {
                    mSlidesAdapter.setList(videoBean.getSlides());
                    binding.linearEmptyView.setVisibility(View.GONE);
                } else binding.linearEmptyView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (open == true){
            binding.rlSlide.setVisibility(View.GONE);
            open = false;
        }else {
            super.onBackPressed();
        }
    }
}
