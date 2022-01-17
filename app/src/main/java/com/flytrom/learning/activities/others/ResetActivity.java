package com.flytrom.learning.activities.others;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.normal_beans.ResetBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.databinding.ActivityResetBinding;
import com.flytrom.learning.databinding.DialogConfirmBinding;
import com.flytrom.learning.databinding.ItemResetBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class ResetActivity extends BaseActivity<ActivityResetBinding> {

    private BaseCustomDialog<DialogConfirmBinding> mConfirmDialog;
    private Call<SuccessBean> mCallResetContent;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_reset;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallResetContent != null) mCallResetContent.cancel();
        super.onDestroy();
    }

    private void initView() {

        binding.toolbar.textTitle.setText(R.string.title_reset);
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);

        SimpleRecyclerViewAdapter<ResetBean, ItemResetBinding> mResetAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_reset, BR.bean, (SimpleRecyclerViewAdapter.SimpleCallback<ResetBean>) (v, resetBean) -> {
            switch (resetBean.getIndex()) {
                case 0:
                    showConfirmPopUp(1);
                    break;
                case 1:
                    showConfirmPopUp(2);
                    break;
                case 2:
                    showConfirmPopUp(3);
                    break;
            }
        });

        binding.rvReset.setLayoutManager(new LinearLayoutManager(this));
        binding.rvReset.setItemAnimator(new DefaultItemAnimator());
        binding.rvReset.setAdapter(mResetAdapter);

        List<ResetBean> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ResetBean bean = new ResetBean(i, Constants.RESET_HEADLINES[i], Constants.RESET_DESCRIPTION[i]);
            list.add(bean);
        }
        mResetAdapter.setList(list);
    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            goBack();
        }
    };

    private void showConfirmPopUp(int type) {
        mConfirmDialog = new BaseCustomDialog<>(this, R.layout.dialog_confirm, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mConfirmDialog.dismiss();
                    break;
                case R.id.text_yes:
                    mConfirmDialog.dismiss();
                    resetContent(type);
                    break;
            }
        });
        Objects.requireNonNull(mConfirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        switch (type) {
            case 1:
                mConfirmDialog.getBinding().textMessage.setText(R.string.title_reset_bookmarks);
                break;
            case 2:
                mConfirmDialog.getBinding().textMessage.setText(R.string.title_reset_qbank);
                break;
            case 3:
                mConfirmDialog.getBinding().textMessage.setText(R.string.title_reset_tests);
                break;
        }
        mConfirmDialog.getBinding().imageIcon.setImageResource(R.drawable.ic_alert);
        mConfirmDialog.getBinding().textYes.setText(R.string.text_yes);
        mConfirmDialog.setCancelable(true);
        mConfirmDialog.show();
    }

    private void resetContent(int type) {
        mCallResetContent = AppController.getInstance().getApis().resetContent(getHeader(), String.valueOf(type));
        mCallResetContent.enqueue(new ResponseHandler<SuccessBean>() {
            @Override
            public void onSuccess(SuccessBean successBean) {
                if (successBean != null) {
                    if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(successBean.getMessage());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<SuccessBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
