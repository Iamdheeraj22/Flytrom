package com.flytrom.learning.pdf_viewer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.MyCustomPagerAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.databinding.ActivityPdfWithViewPagerBinding;
import com.flytrom.learning.utils.PrefUtils;

import java.util.Random;

public class PDFViewActivityWithViewPager extends BaseActivity<ActivityPdfWithViewPagerBinding> {

    public static final int RANDOM_TEXT_DURATION = 5000;
    private Random mRandom;
    private Handler mHandler;
    private MyCustomPagerAdapter mPagerAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pdf_with_view_pager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(runnable, RANDOM_TEXT_DURATION);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView(Intent intent) {
        mPagerAdapter = new MyCustomPagerAdapter(this);
        setBaseCallback(baseCallback);
        mRandom = new Random();
        mHandler = new Handler();
        binding.textEmail.setText(PrefUtils.getInstance().getUser().getEmail());
        if (intent == null) {
            finishActivityWithBackAnim();
            return;
        }
        getSelectedPosition();
    }

    private void getSelectedPosition() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mPagerAdapter.setList(PDFViewActivity.getInstance().getNotesList());
            binding.viewPager.setAdapter(mPagerAdapter);

        }
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_previous:
                if (binding.viewPager.getCurrentItem() > 0)
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() - 1);
                break;
            case R.id.image_next:
                if (binding.viewPager.getCurrentItem() < mPagerAdapter.getDataList().size())
                    binding.viewPager.setCurrentItem(binding.viewPager.getCurrentItem() + 1);
                break;
        }
    };

    private Runnable runnable = this::setRandomTextOnScreen;

    private void setRandomTextOnScreen() {
        float dx = mRandom.nextFloat() * binding.view.getWidth();
        float dy = mRandom.nextFloat() * binding.view.getHeight();

        if (dx > 200 && dy > 200) {
            if (dx < binding.view.getWidth() && dy < binding.view.getHeight()) {

                binding.textEmail.animate()
                        .x(dx)
                        .y(dy)
                        .setDuration(0)
                        .start();

                mHandler.postDelayed(runnable, RANDOM_TEXT_DURATION);
            }
        } else setRandomTextOnScreen();
    }
}
