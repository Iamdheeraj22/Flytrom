package com.flytrom.learning.activities.info;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.TermsConditionsAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.TermsConditionBean;
import com.flytrom.learning.databinding.ActivityTermsConditionsBinding;
import com.flytrom.learning.utils.Constants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TermsConditionsActivity extends BaseActivity<ActivityTermsConditionsBinding> {

    private TermsConditionsAdapter mTermsConditionsAdapter;
    private String mFrom;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_terms_conditions;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {
        if (getIntent().getExtras() != null) {
            mFrom = getIntent().getStringExtra("from");
        }
        binding.toolbar.textTitle.setText(getString(R.string.text_terms_conditions));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);

        mTermsConditionsAdapter = new TermsConditionsAdapter(this, (v, termsConditionBean) -> {

            if (v.getId() == R.id.relative_item) {
                if (termsConditionBean.isSelected()) termsConditionBean.setSelected(false);
                else termsConditionBean.setSelected(true);
                mTermsConditionsAdapter.notifyItemChanged(termsConditionBean.getIndex());
            }
        });

        binding.rvTerms.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTerms.setItemAnimator(new DefaultItemAnimator());
        binding.rvTerms.setAdapter(mTermsConditionsAdapter);

        setListOnAdapter();
    }

    private void setListOnAdapter() {
        String[] myMessage = new String[0];
        try {
            Resources res = getResources();
            InputStream in_s = null;
            if (mFrom != null) {
                if (mFrom.equals("Terms")) {
                    in_s = res.openRawResource(R.raw.terms_data);
                } else {
                    in_s = res.openRawResource(R.raw.faqs_data);
                    binding.toolbar.textTitle.setText(R.string.title_faqs);
                    binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                    binding.textHeader.setText(R.string.title_faqs);
                }
            }
            if (in_s != null) {
                byte[] b = new byte[in_s.available()];
                in_s.read(b);
                String s = new String(b);
                myMessage = s.split("&&");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TermsConditionBean> list = new ArrayList<>();
        if (myMessage.length > 0) {
            if (mFrom != null) {
                if (mFrom.equals("Terms")) {
                    for (int i = 0; i < Constants.TERMS_HEADLINES.length; i++) {
                        TermsConditionBean bean = new TermsConditionBean(i, null, Constants.TERMS_HEADLINES[i], myMessage[i], false);
                        list.add(bean);
                    }
                } else {
                    for (int i = 0; i < Constants.FAQ_HEADLINES.length; i++) {
                        TermsConditionBean bean = new TermsConditionBean(i, Constants.FAQ_TITLES[i], Constants.FAQ_HEADLINES[i], myMessage[i], false);
                        list.add(bean);
                    }
                }
            }

            mTermsConditionsAdapter.setDataList(list);
        }
    }

    private BaseCallback baseCallback = view -> {

        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };
}
