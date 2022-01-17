package com.flytrom.learning.fragments;

import android.os.Bundle;
import android.view.View;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.databinding.FragmentSlidesBinding;

public class SlidesFragment extends BaseFragment<FragmentSlidesBinding> {

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);

        initView();
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_slides;
    }

    private void initView() {

        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback=new BaseCallback() {
        @Override
        public void onClick(View view) {

        }
    };
}
