package com.flytrom.learning.pdf_viewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.flytrom.learning.R;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;

public class PDFView {

    static abstract class BaseBuilder {

        PDFConfig config;
        VideoBean videoBean;

        BaseBuilder(Context context) {
            this.config = new PDFConfig();
        }
    }

    public static abstract class Builder extends BaseBuilder {

        public Builder(Activity activity) {
            super(activity);
        }

        public Builder(Fragment fragment) {
            super(fragment.getActivity());
        }

        public Builder fromFilePath(String filepath) {
            config.setFilepath(filepath);
            return this;
        }

        public Builder videoBean(VideoBean videoBean) {
            this.videoBean = videoBean;
            return this;
        }


        public Builder swipeHorizontal(boolean swipeOrientation) {
            config.setSwipeorientation(swipeOrientation ? 0 : 1);
            return this;
        }

        /*public Builder fromURL(String url) {
            config.setNetwork_url(url);
            return this;
        }*/

        public abstract void start();

    }

    static class ActivityBuilder extends Builder {
        private Activity activity;

        ActivityBuilder(Activity activity) {
            super(activity);
            this.activity = activity;
        }

        @Override
        public void start() {
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoBean", videoBean);
            Intent intent = new Intent(activity, PDFViewActivity.class);
            //intent.putExtra(PDFConfig.EXTRA_CONFIG, config);
            intent.putExtras(bundle);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }
    }

    public static Builder with(Activity activity) {
        return new ActivityBuilder(activity);
    }

}
