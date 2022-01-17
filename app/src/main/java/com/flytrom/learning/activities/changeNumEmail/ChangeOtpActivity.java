package com.flytrom.learning.activities.changeNumEmail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.info.ProfileActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.ChangeEmailPhoneModel;
import com.flytrom.learning.model.EmailOtp;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.model.SendOtpPojo;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.SmsListener;
import com.flytrom.learning.utils.SmsReceiver;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.Locale;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeOtpActivity extends BaseActivity {
    Apis apiInterface,getApiInterface;
    public int counter;
    private static final long START_TIME_IN_MILES = 60000;
    private CountDownTimer countDownTimer;
    private long mTimeLeft = START_TIME_IN_MILES;
    String number,otp,country,countryCodeNum,num,email,otpEmail,countryCode,phoneEmail;
    OtpTextView edOtp;
    TextView timer,resend,mob;
    LinearLayout llOTPTXT2,llOtpL2;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);

        apiInterface = AppController.getInstance().getApisOtp();
        getApiInterface = AppController.getInstance().getApis();
        edOtp = findViewById(R.id.edOtp2);
        resend = findViewById(R.id.resend2);
        mob = findViewById(R.id.mob);
        timer = findViewById(R.id.timer2);
        llOTPTXT2 = findViewById(R.id.llOTPTXT2);
        llOtpL2 = findViewById(R.id.llOtpL2);
        String wantPermission = Manifest.permission.GET_ACCOUNTS;

        startSMSListener();

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                if (!messageText.equalsIgnoreCase("")) {
                    edOtp.setOTP(messageText);
                }
            }
        });

//        if (!checkPermission(wantPermission)) {
//            requestPermission(wantPermission);
//        } else {
//            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
//            Account[] accounts = AccountManager.get(this).getAccounts();
//            for (Account account : accounts) {
//                if (emailPattern.matcher(account.name).matches()) {
//                    String TAG = "MainActivity";
//                    phoneEmail = account.name;
//                }
//            }
//        }


        Intent in = getIntent();
        if (in!=null){
            number = in.getStringExtra("otpNum");
            country = in.getStringExtra("country");
            countryCodeNum = in.getStringExtra("countryCodeNum");
            num = in.getStringExtra("number");
            countryCode = in.getStringExtra("countryCode");
        }

        Intent inte = getIntent();
        if (inte != null){
            email = inte.getStringExtra("email");
        }else {

        }
        if(num == null){

        }else {
            startTimer();
            resendPhoneOtp();
            mob.setText("Please enter the codesent to you on "+"("+countryCode+")"+ num);
        }
        if (email != null){
            startTimer();
            emailVerificationCode();
            mob.setText("Please enter the codesent to you on "+ email);

        }else {

        }

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {

            @Override
            public void onVisibilityChanged(boolean isOpen) {

                if (isOpen){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 0);
                    llOtpL2.setLayoutParams(params);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params2.setMargins(0, 10, 0, 0);
                    llOTPTXT2.setLayoutParams(params2);
                }
                else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 140, 0, 0);
                    llOtpL2.setLayoutParams(params);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params2.setMargins(0, 300, 0, 0);
                    llOTPTXT2.setLayoutParams(params2);

                }

            }
        });



    }

    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "Get account permission allows us to get your email", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }


    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

//                Toast.makeText(ChangeOtpActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ChangeOtpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    public void verifyChangeOtp(View view) {
        if (num != null){
            changePhoneNum();
        }else if (email != null){
            changeEmail();
        }
    }

    private void changePhoneNum() {
        getApiInterface.changePhone(getHeader(),num,countryCodeNum,country).enqueue(new Callback<ChangeEmailPhoneModel>() {
            @Override
            public void onResponse(Call<ChangeEmailPhoneModel> call, Response<ChangeEmailPhoneModel> response) {
               if (response.isSuccessful()){
                   Intent intent = new Intent(ChangeOtpActivity.this, ProfileActivity.class);
                   startActivity(intent);
                   finish();
               }

            }

            @Override
            public void onFailure(Call<ChangeEmailPhoneModel> call, Throwable t) {
                Log.d("QWERTYU2",t.getLocalizedMessage());
            }
        });
    }
    private void changeEmail() {
        getApiInterface.changeEmail(getHeader(),email).enqueue(new Callback<ChangeEmailPhoneModel>() {
            @Override
            public void onResponse(Call<ChangeEmailPhoneModel> call, Response<ChangeEmailPhoneModel> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(ChangeOtpActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            @Override
            public void onFailure(Call<ChangeEmailPhoneModel> call, Throwable t) {
                Log.d("QWERTYU2",t.getLocalizedMessage());
            }
        });
    }

    public void resendChangeOtp(View view) {
        if (num != null){
            resendPhoneOtpAgain();
        }else if (email != null){
            emailVerificationCode();
        }


    }

    private void resendPhoneOtp() {
        apiInterface.sendOtp("60f94500c159964182726282",number,"6","307530Agy6ftmPQ5decf88c","Flytro").enqueue(new Callback<SendOtpPojo>() {
            @Override
            public void onResponse(Call<SendOtpPojo> call, Response<SendOtpPojo> response) {
                if (response.isSuccessful()){
                    if (response.body().type.equalsIgnoreCase("success")){
                        startTimer();
                    }else {
                        Toast.makeText(ChangeOtpActivity.this, "Please enter Number", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("ERRORNEW",response.body().type);
                }
            }

            @Override
            public void onFailure(Call<SendOtpPojo> call, Throwable t) {
                Log.d("ERROR",t.getLocalizedMessage());
            }
        });
    }

    private void emailVerificationCode() {
        getApiInterface.emailVerificationCode(email).enqueue(new Callback<EmailOtp>() {
            @Override
            public void onResponse(Call<EmailOtp> call, Response<EmailOtp> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ChangeOtpActivity.this, "Otp sent success", Toast.LENGTH_SHORT).show();
//                    if (email.equalsIgnoreCase(phoneEmail)){
////                        edOtp.setOTP(String.valueOf(response.body().getData()));
//                    }

                    otpEmail = String.valueOf(response.body().getData());
                }else {
                    Toast.makeText(ChangeOtpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmailOtp> call, Throwable t) {
                Toast.makeText(ChangeOtpActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(mTimeLeft,1000) {
            @Override
            public void onTick(long l) {
                mTimeLeft = l;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timer.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);

            }
        }.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_otp;
    }

    private void updateCountDownText() {
        int mins = (int) (mTimeLeft / 1000)/60;
        int sec = (int) (mTimeLeft / 1000)%60;
        String timeFormant = String.format(Locale.getDefault(),"%02d:%02d",mins,sec);

        timer.setText("Resend Code in "+timeFormant);
    }


    public void backChangeOtp(View view) {
        onBackPressed();
        finish();
    }

    private void resendPhoneOtpAgain() {
        apiInterface.retryOtp("307530Agy6ftmPQ5decf88c","text",number).enqueue(new Callback<SendOtpPojo>() {
            @Override
            public void onResponse(Call<SendOtpPojo> call, Response<SendOtpPojo> response) {
                if (response.isSuccessful()){
                    startTimer();
                    Toast.makeText(ChangeOtpActivity.this, "Otp send Successful Retry", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("ERRORNEW",response.body().type);
                }
            }

            @Override
            public void onFailure(Call<SendOtpPojo> call, Throwable t) {
                Log.d("ERROR",t.getLocalizedMessage());
            }
        });
    }


    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            return result == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
//            permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
//                Account[] accounts = AccountManager.get(this).getAccounts();
//                for (Account account : accounts) {
//                    if (emailPattern.matcher(account.name).matches()) {
//                        String TAG = "MainActivity";
//                        Log.d(TAG, String.format("%s - %s", account.name, account.type));
//                        phoneEmail = account.name;
//                    }
//                }
//            } else {
//                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}