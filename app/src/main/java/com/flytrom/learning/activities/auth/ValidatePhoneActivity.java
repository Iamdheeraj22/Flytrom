package com.flytrom.learning.activities.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.databinding.ActivityValidatePhoneBinding;
import com.flytrom.learning.utils.UtilMethods;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class ValidatePhoneActivity extends BaseActivity<ActivityValidatePhoneBinding> {

    private FirebaseAuth mAuth;
    private String mVerificationId;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        mAuth = FirebaseAuth.getInstance();
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_validate_phone;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {


    }

    private void sendVerificationCode(String mobile) {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91"+mobile)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "+91" + mobile,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();

            if (code != null) {
                binding.editTextOtp.setText(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ValidatePhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NotNull String s, @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        if (mVerificationId != null && code != null) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ValidatePhoneActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent();
                        intent.putExtra("number", binding.editTextMobileNumber.getText().toString());
                        setResult(121, intent);
                        finishActivityWithBackAnim();
                    } else {

                        String message = Resources.getSystem().getString(R.string.something_went_wrong);

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = Resources.getSystem().getString(R.string.invalid_code);
                        }

                        showToast(message);
                    }
                });
    }

    private void initView() {

        setBaseCallback(baseCallback);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.button_continue:
                UtilMethods.hideKeyboard(this);
                if (binding.llPhoneNumber.getVisibility() == View.VISIBLE) {
                    if (UtilMethods.validatePhoneNumber(binding.editTextMobileNumber.getText().toString())) {
                        binding.textEnterNumber.setText(String.format("Please enter the code sent to\n+91 %s", binding.editTextMobileNumber.getText().toString()));
                        binding.llPhoneNumber.setVisibility(View.GONE);
                        binding.llCode.setVisibility(View.VISIBLE);
                        sendVerificationCode(binding.editTextMobileNumber.getText().toString());
                    } else {
                        showToast(Resources.getSystem().getString(R.string.enter_valid_phone_number));
                    }
                } else {

                    if (binding.editTextOtp.getText().toString().equalsIgnoreCase("")) {
                        showToast(Resources.getSystem().getString(R.string.enter_otp));
                    } else {
                        verifyVerificationCode(binding.editTextOtp.getText().toString());
                    }
                }
                break;
        }
    };
}

