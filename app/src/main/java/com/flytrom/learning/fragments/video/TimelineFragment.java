package com.flytrom.learning.fragments.video;


import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.others.TopicsBean;
import com.flytrom.learning.databinding.FragmentTimelineBinding;
import com.flytrom.learning.databinding.ItemVideoTopicBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends BaseFragment<FragmentTimelineBinding> {

    private SimpleRecyclerViewAdapter<TopicsBean, ItemVideoTopicBinding> mVideoTopicsAdapter;

    public TimelineFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_timeline;
    }

    private void initView() {
        mVideoTopicsAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_video_topic, BR.bean, (v, groupBean) -> {
           /* if (mVdoplayer != null) {
                try {
                    int seconds = Integer.parseInt(groupBean.getMinutes()) * 60;
                    seconds = seconds + Integer.parseInt(groupBean.getSeconds());
                    //mVdoplayer.seekTo(Integer.parseInt(groupBean.getDuration()) * 1000);
                    mVdoplayer.seekTo(seconds * 1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }*/
        });
        binding.recyclerVideoTopics.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerVideoTopics.setAdapter(mVideoTopicsAdapter);
    }

    public void setData(){

    }
}