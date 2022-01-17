package com.flytrom.learning.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.flytrom.learning.fragments.video.TimelineFragment;

/**
 * Created by Loveraj on 13-01-2020.
 */
public class VideoPagerAdapter extends FragmentPagerAdapter {

    public VideoPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TimelineFragment();
            case 1:
                return new TimelineFragment();
            default:
                throw new IllegalArgumentException("unexpected viewType at :" + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "TIMELINE" : "DISCUSS DOUBTS";
    }
}
