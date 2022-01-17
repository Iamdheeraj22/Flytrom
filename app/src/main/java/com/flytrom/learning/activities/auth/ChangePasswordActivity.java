package com.flytrom.learning.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.LoginBean;
import com.flytrom.learning.databinding.ActivityChangePasswordBinding;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class ChangePasswordActivity extends BaseActivity<ActivityChangePasswordBinding> {

    private FieldsValidator mFieldsValidator;
    private Call<LoginModel> mCallChangePassword;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        initView();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallChangePassword != null) mCallChangePassword.cancel();
        super.onDestroy();
    }

    private void initView() {
        mFieldsValidator = new FieldsValidator();
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.image_eye_old_pass:
                showHidePassOldPass();
                break;
            case R.id.image_eye_new_password:
                showHidePassNewPass();
                break;
            case R.id.image_eye_confirm_pass:
                showHidePassConfirmPass();
                break;
            case R.id.button_change:
                if (validatePassword()) changePasswordApi();
                break;
        }
    };

    private boolean validatePassword() {
        return mFieldsValidator.hasText(binding.editTextOldPass) &&
                mFieldsValidator.hasText(binding.editTextNewPass) &&
                mFieldsValidator.hasText(binding.editTextConfirmPass) &&
                mFieldsValidator.validatePasswordMatch(binding.editTextNewPass,
                        binding.editTextConfirmPass, "Password not matched");
    }

    public void showHidePassOldPass() {

        if (binding.editTextOldPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            binding.imageEyeOldPass.setImageResource(R.drawable.ic_open_eye);
            //Show Password
            binding.editTextOldPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.imageEyeOldPass.setImageResource(R.drawable.ic_hide_eye);
            //Hide Password
            binding.editTextOldPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        binding.editTextOldPass.requestFocus(binding.editTextOldPass.getText().length() - 1);
    }

    public void showHidePassNewPass() {

        if (binding.editTextNewPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            binding.imageEyeNewPassword.setImageResource(R.drawable.ic_open_eye);
            //Show Password
            binding.editTextNewPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.imageEyeNewPassword.setImageResource(R.drawable.ic_hide_eye);
            //Hide Password
            binding.editTextNewPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        binding.editTextNewPass.requestFocus(binding.editTextNewPass.length() - 1);
    }

    public void showHidePassConfirmPass() {

        if (binding.editTextConfirmPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            binding.imageEyeConfirmPass.setImageResource(R.drawable.ic_open_eye);
            //Show Password
            binding.editTextConfirmPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.imageEyeConfirmPass.setImageResource(R.drawable.ic_hide_eye);
            //Hide Password
            binding.editTextConfirmPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        binding.editTextConfirmPass.requestFocus(binding.editTextConfirmPass.length() - 1);
    }

    private void changePasswordApi() {
        showBaseProgress();
        Map<String, String> map = new HashMap<>();
        map.put("content-type", "application/json");
        map.put("oldpassword", binding.editTextOldPass.getText().toString());
        map.put("newpassword", binding.editTextNewPass.getText().toString());

        mCallChangePassword = AppController.getInstance().getApis().changePassword(getHeader(), map);
        mCallChangePassword.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(model.getMessage());
                        goBack();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null)
                    showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_change_password, Throwable t) {
                onCallComplete(call_change_password, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
