package com.flytrom.learning.activities.otp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.SignUpActivity;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.EmailOtp;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.model.SendOtpPojo;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.SmsListener;
import com.flytrom.learning.utils.SmsReceiver;
import com.google.android.exoplayer2.C;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {
    Apis apiInterface,getApiInterface;
    String number,otp,country,countryCodeNum,num,email,otpEmail,countryCode,phoneEmail;
    OtpTextView edOtp;
    TextView timer,resend,naum;
    public int counter;
    private String mFireBaseToken;
    private static final long START_TIME_IN_MILES = 60000;
    private CountDownTimer countDownTimer;
    private long mTimeLeft = START_TIME_IN_MILES;
    LinearLayout otpLL,llOtpL,llOTPTXT;
    RelativeLayout rlTime,relativeSize;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        if (mFireBaseToken != null) {

        } else {
            getToken();
        }
        startSMSListener();
        relativeSize = findViewById(R.id.relativeSize);
        final ScrollView scrollTuch = findViewById(R.id.scrollTuch);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);
        String wantPermission = Manifest.permission.RECEIVE_SMS;
        relativeSize.getLayoutParams().height = displayMetrics.heightPixels;

        scrollTuch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        scrollTuch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollTuch.post(new Runnable() {
                    @Override
                    public void run() {
//                        scrollTuch.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            Pattern emailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (emailPattern.matcher(account.name).matches()) {
                    String TAG = "MainActivity";
                    phoneEmail = account.name;
                }
            }
        }




        apiInterface = AppController.getInstance().getApisOtp();
        getApiInterface = AppController.getInstance().getApis();
        edOtp = findViewById(R.id.edOtp);
        naum = findViewById(R.id.naum);
        resend = findViewById(R.id.resend);
        timer = findViewById(R.id.timer);
        llOtpL = findViewById(R.id.llOtpL);
        llOTPTXT = findViewById(R.id.llOTPTXT);


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
            sendOtp();
            naum.setText("Please enter the code sent to you on "+"("+countryCode+")"+num);
        }
        if (email != null){
            startTimer();
            emailVerificationCode();
            naum.setText("Please enter the code sent to you on "+email);
        }else {

        }
        Log.d("SDTYHJ",email+ "Num"+country+"Conutry"+num);

        otpLL=findViewById(R.id.otpLL);
        rlTime=findViewById(R.id.rlTime);

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {

            @Override
            public void onVisibilityChanged(boolean isOpen) {

                if (isOpen){
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 10, 0, 0);
                    llOtpL.setLayoutParams(params);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params2.setMargins(0, 10, 0, 0);
                    llOTPTXT.setLayoutParams(params);
                }
                else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 140, 0, 0);
                    llOtpL.setLayoutParams(params);

                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params2.setMargins(0, 300, 0, 0);
                    llOTPTXT.setLayoutParams(params2);

                }

            }
        });

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                if (!messageText.equalsIgnoreCase("")) {
                    edOtp.setOTP(messageText);
                }
            }
        });

    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(this);
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

//                Toast.makeText(OtpActivity.this, "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OtpActivity.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void verifyOtp(View view) {
        otp = edOtp.getOtp().toString();
//       if (otp.equalsIgnoreCase(otpEmail)){
//           Intent intent2 = new Intent(OtpActivity.this, SignUpActivity.class);
//           intent2.putExtra("email",email);
//           startActivity(intent2);
//       }else
       if (email != null){
           loginWithEmailId();
       }
       if (num != null){
           verifyOtpApi();
       }

    }

    private void verifyOtpApi() {
        apiInterface.verifyOtp("307530Agy6ftmPQ5decf88c",number,otp).enqueue(new Callback<SendOtpPojo>() {
            @Override
            public void onResponse(Call<SendOtpPojo> call, Response<SendOtpPojo> response) {
                if (response.isSuccessful()){
                    if (response.body().type.equalsIgnoreCase("success")){
                        loginWithNumber();
                        Toast.makeText(OtpActivity.this, "Otp Verify Successful", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(OtpActivity.this, "Please enter valid Otp", Toast.LENGTH_SHORT).show();
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

    private void loginWithNumber() {
        getApiInterface.loginWithPhone(num,countryCodeNum,country,Constants.DEVICETYPE,mFireBaseToken).
                enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel loginModel = response.body();
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 202){
                        Intent intent = new Intent(OtpActivity.this, SignUpActivity.class);
                        intent.putExtra("otpNum",number);
                        intent.putExtra("country",country);
                        intent.putExtra("countryCodeNum",countryCodeNum);
                        intent.putExtra("number",num);
                        startActivity(intent);
                    }else if (response.body().getStatus() == 200){

                        if (PrefUtils.getInstance().setUser(loginModel.getData())) {
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.EMAIL,loginModel.getData().getEmail());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.NUMBER,loginModel.getData().getPhone_number());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.USER_ID,loginModel.getData().getId());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.FIRST_NAME, loginModel.getData().getFirst_name()+" "+loginModel.getData().getLast_name());
                            if (loginModel != null) {
                                if (loginModel.getStatus() == Constants.SUCCESS_CODE) {
                                    if (PrefUtils.getInstance().setUser(loginModel.getData())) {
                                        goToHomeScreen();
                                    }
                                }
                            }
                        }


                    }
                }else {
                    Toast.makeText(OtpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(OtpActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void emailVerificationCode() {
        getApiInterface.emailVerificationCode(email).enqueue(new Callback<EmailOtp>() {
            @Override
            public void onResponse(Call<EmailOtp> call, Response<EmailOtp> response) {
                if (response.isSuccessful()){
                  //  Toast.makeText(OtpActivity.this, "Otp sent success", Toast.LENGTH_SHORT).show();
//                    if (email.equalsIgnoreCase(phoneEmail)){
////                        edOtp.setOTP(String.valueOf(response.body().getData()));
//                    }
                    if (response.body().getStatus() == 200){
                        otpEmail = "";
                        otpEmail = String.valueOf(response.body().getData());
                        Log.d("Code"," : "+otpEmail);
                    }else {
                        Toast.makeText(OtpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<EmailOtp> call, Throwable t) {
                Toast.makeText(OtpActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goToHomeScreen() {
        if (PrefUtils.getInstance().getUser() != null) {

            UserDataBean loginBean = PrefUtils.getInstance().getUser();

            Intent intent;

            if (loginBean != null) {
                intent = new Intent(OtpActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }
    }


    private void sendOtp() {
        apiInterface.sendOtp("60f94500c159964182726282",number,"6","307530Agy6ftmPQ5decf88c","FLTROM").enqueue(new Callback<SendOtpPojo>() {
            @Override
            public void onResponse(Call<SendOtpPojo> call, Response<SendOtpPojo> response) {
                if (response.isSuccessful()){
                    if (response.body().type.equalsIgnoreCase("success")){
                        startTimer();
                        Log.d("Code"," : "+response.body().getType());
                    }else {
                        Toast.makeText(OtpActivity.this, "Please enter Number", Toast.LENGTH_SHORT).show();
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

    private void startTimer() {
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

    private void updateCountDownText() {
        int mins = (int) (mTimeLeft / 1000)/60;
        int sec = (int) (mTimeLeft / 1000)%60;
        String timeFormant = String.format(Locale.getDefault(),"%02d:%02d",mins,sec);

        timer.setText("Resend Code in "+timeFormant);
    }

    private void retryOtpApi() {
        apiInterface.retryOtp("307530Agy6ftmPQ5decf88c","text",number).enqueue(new Callback<SendOtpPojo>() {
            @Override
            public void onResponse(Call<SendOtpPojo> call, Response<SendOtpPojo> response) {
                if (response.isSuccessful()){
                    startTimer();
                    Toast.makeText(OtpActivity.this, "Otp send Successful Retry", Toast.LENGTH_SHORT).show();
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


    public void resendOtp(View view) {
        startTimer();
        if (number != null){
            retryOtpApi();
        }else if (email != null){
            emailVerificationCode();
        }

    }

    public void backOtp(View view) {
        onBackPressed();
        finish();
    }

    public void changeNumber(View view) {
        onBackPressed();
        finish();
    }

    private void loginWithEmailId() {
        getApiInterface.loginWithEmail(email, Constants.DEVICETYPE,mFireBaseToken).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel loginModel = response.body();
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 202){
                        Intent intent2 = new Intent(OtpActivity.this, SignUpActivity.class);
                        intent2.putExtra("email",email);
                        startActivity(intent2);
                    }else if (response.body().getStatus() == 200){
                        if (PrefUtils.getInstance().setUser(loginModel.getData())) {
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.EMAIL,loginModel.getData().getEmail());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.NUMBER,loginModel.getData().getPhone_number());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.USER_ID,loginModel.getData().getId());
                            MySharedPreferences.getInstance().saveString(OtpActivity.this, ConstantsNew.FIRST_NAME, loginModel.getData().getFirst_name()+" "+loginModel.getData().getLast_name());
                            if (loginModel != null) {
                                if (loginModel.getStatus() == Constants.SUCCESS_CODE) {
                                    if (PrefUtils.getInstance().setUser(loginModel.getData())) {
                                        goToHomeScreen();
                                    }
                                }
                            }

                        }


                    }
                }else {
                    Toast.makeText(OtpActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(OtpActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Log.d("TOKEN", instanceIdResult.getToken());
//                mFireBaseToken = instanceIdResult.getToken();

            }
        });

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("getInstanceIdfailed", task.getException());
                            return;
                        }
                        mFireBaseToken = task.getResult().getToken();
                        Log.d("TOKENNNNN", mFireBaseToken);
//                        ((SplashActivity) activity).sharePref.put(FCM_TOKEN, fcmToken);
                    }
                });

    }
    protected void showErrorToast(String message) {
        Toast.makeText(this, message + "", Toast.LENGTH_SHORT).show();
        //MyToast.error(context, "" + t, Toast.LENGTH_LONG, true).show();
    }

    private boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            return result == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }
    private void requestPermission(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Toast.makeText(this, "Get account permission allows us to get your email", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Pattern emailPattern = Patterns.EMAIL_ADDRESS;
                Account[] accounts = AccountManager.get(this).getAccounts();
                for (Account account : accounts) {
                    if (emailPattern.matcher(account.name).matches()) {
                        String TAG = "MainActivity";
                        Log.d(TAG, String.format("%s - %s", account.name, account.type));
                        phoneEmail = account.name;
                    }
                }
            } else {
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_LONG).show();
            }
        }
    }

}