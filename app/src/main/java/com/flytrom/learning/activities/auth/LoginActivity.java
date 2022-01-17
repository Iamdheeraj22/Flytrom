package com.flytrom.learning.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.LoginBean;
import com.flytrom.learning.databinding.ActivityLoginBinding;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private FieldsValidator mFieldsValidator;
    private String mFireBaseToken;
    private Call<LoginModel> mCallLogin;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        if (mCallLogin != null) mCallLogin.cancel();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToken();
    }

    private void initView() {
        mFieldsValidator = new FieldsValidator();
        setHighLightedText(binding.textRegister, getString(R.string.don_t_have_an_account_register), 23, 31);
        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.text_forgot_pass:
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                goNextAnimation();
                break;
            case R.id.button_login:
                if (validate()) loginApi();
                break;
            case R.id.image_password:
                showHidePassOldPass();
                break;
        }
    };

    public void showHidePassOldPass() {

        if (binding.editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            binding.imagePassword.setImageResource(R.drawable.ic_open_eye);
            //Show Password
            binding.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            binding.imagePassword.setImageResource(R.drawable.ic_hide_eye);
            //Hide Password
            binding.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        binding.editTextPassword.requestFocus(binding.editTextPassword.getText().length() - 1);
    }

    public void setHighLightedText(TextView tv, String text, int start, int end) {
        Spannable wordToSpan = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivityForResult(new Intent(LoginActivity.this, ValidatePhoneActivity.class), 121);
                goNextAnimation();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.blue));
            }
        };
        wordToSpan.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 121) {
            String number = null;
            if (data != null) {
                number = data.getStringExtra("number");
            }
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            intent.putExtra("number", number);
            startActivity(intent);
            finishActivityWithBackAnim();
        }
    }

    private boolean validate() {
        return mFieldsValidator.hasText(binding.editTextEmail) &&
                mFieldsValidator.isEmailAddress(binding.editTextEmail, true) &&
                mFieldsValidator.hasText(binding.editTextPassword);
    }

    private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                try {
                    mFireBaseToken = Objects.requireNonNull(task.getResult()).getToken();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                showToast(getString(R.string.no_internet));
                //showErrorToast(getString(R.string.not_valid_token));
            }
        });
    }

    private void loginApi() {
        showBaseProgress();
        HashMap<String, String> map = new HashMap<>();
        map.put("email", binding.editTextEmail.getText().toString());
        map.put("password", binding.editTextPassword.getText().toString());
        map.put("device_type", Constants.DEVICETYPE);
        if (mFireBaseToken != null) {
            map.put("device_token", mFireBaseToken);
        } else {
            hideBaseProgress();
            getToken();
            return;
        }

        mCallLogin = AppController.getInstance().getApis().login(map);
        mCallLogin.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel loginBean) {

                if (loginBean != null) {
                    if (loginBean.getStatus() == Constants.SUCCESS_CODE) {
                        if (PrefUtils.getInstance().setUser(loginBean.getData())) {
                            goToHomeScreen();
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }


            @Override
            public void onComplete(Call<LoginModel> call, Throwable t) {
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
