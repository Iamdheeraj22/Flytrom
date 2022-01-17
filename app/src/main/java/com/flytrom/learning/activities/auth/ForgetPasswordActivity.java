package com.flytrom.learning.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.SignUpBean;
import com.flytrom.learning.databinding.ActivityForgetPasswordBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.ResponseHandler;

import retrofit2.Call;

public class ForgetPasswordActivity extends BaseActivity<ActivityForgetPasswordBinding> {

    private Call<SignUpBean> callForgotPassword;
    private FieldsValidator mFieldsValidator;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        mFieldsValidator = new FieldsValidator();

        setBaseCallback(view -> {
            switch (view.getId()) {
                case R.id.image_back:
                    goBack();
                    break;
                case R.id.button_submit:
                    if (validate()) forgetPasswordApi();
                    break;
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void onDestroy() {
        if (callForgotPassword != null) callForgotPassword.cancel();
        super.onDestroy();
    }

    private boolean validate() {
        return mFieldsValidator.hasText(binding.editTextEmail) && mFieldsValidator.isEmailAddress(binding.editTextEmail, true);
    }

    private void forgetPasswordApi() {

        showBaseProgress();
        callForgotPassword = AppController.getInstance().getApis().forgotPassword(binding.editTextEmail.getText().toString());
        callForgotPassword.enqueue(new ResponseHandler<SignUpBean>() {
            @Override
            public void onSuccess(SignUpBean signUpBean) {

                if (signUpBean != null) {
                    if (signUpBean.getStatus() == Constants.SUCCESS_CODE) {
                        showSuccessToast(signUpBean.getMessage());
                        goBack();
                    }
                }

            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());

            }

            @Override
            public void onComplete(Call<SignUpBean> call, Throwable t) {
                hideBaseProgress();
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