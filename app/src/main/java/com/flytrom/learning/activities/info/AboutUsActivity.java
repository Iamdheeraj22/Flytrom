package com.flytrom.learning.activities.info;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> {

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_about_us;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        binding.toolbar.textTitle.setText(getString(R.string.text_about_us));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        binding.textViewFlytrom.setText(Html.fromHtml("<b>Flytrom Pvt Ltd </b>is headquartered in Gurgaon (INDIA)."));
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {

        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

}
