package com.flytrom.learning.activities.auth;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.model.regionLodel;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.flytrom.learning.R;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.SignUpBean;
import com.flytrom.learning.databinding.ActivitySignUpBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.FieldsValidator;
import com.flytrom.learning.utils.ResponseHandler;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends BaseActivity<ActivitySignUpBinding> {

    private FieldsValidator mFieldsValidator;
    private Call<LoginModel> mCallSignUp;
    private Call<SignUpBean> mCallCheckEmailAvail;
    private HashMap<String, String> mMapValues;
    private String mFireBaseToken;
    PopupMenu popupMenu,popupMenu2;
    Apis apisInterface;
    String number,regionId = "",country,countryCodeNum,num,email;
    TextView region,spinner_designation;
    private String mPhoneNumber,destId = "";

    Calendar calendar;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    String dob = "";
    int day, month, year;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
        region = findViewById(R.id.region);
        calendar = Calendar.getInstance();
        spinner_designation = findViewById(R.id.spinner_designation);
        apisInterface = AppController.getInstance().getApis();

        Intent in = getIntent();
        if (in!=null){
            number = in.getStringExtra("otpNum");
            countryCodeNum = in.getStringExtra("countryCodeNum");
            num = in.getStringExtra("number");
        }else {
            number = "";
            countryCodeNum = "";
            num = "";
        }

        if (num == null){
            number = "";
            countryCodeNum = "";
            num = "";
        }else {
            binding.editTextEmail.setVisibility(View.VISIBLE);
            binding.editTextNumber.setVisibility(View.GONE);
        }

        Intent inte = getIntent();
        if (inte != null){
            email = inte.getStringExtra("email");
            binding.editTextEmail.setText(email);
        }else {
            email = "";
        }
        if (email == null){
            email = "";

        }else {
            binding.editTextNumber.setVisibility(View.VISIBLE);
        }

        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                /* if (mCallCheckEmailAvail != null) mCallCheckEmailAvail.cancel();
                checkEmailAvailability(s.toString());*/
            }
        });
        reasonApi();
        popup();
    }

    private void reasonApi() {

        apisInterface.getRegions().enqueue(new Callback<regionLodel>() {
            @Override
            public void onResponse(Call<regionLodel> call, Response<regionLodel> response) {
                if (response.isSuccessful()){
                    regionLodel RegionLodel = response.body();

                    popupMenu = new PopupMenu(SignUpActivity.this, region);
                    for (int i = 0; i <RegionLodel.getData().size() ; i++) {
                        popupMenu.getMenu().add(0, Integer.parseInt(RegionLodel.getData().get(i).getId()),0,RegionLodel.getData().get(i).getName());
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            region.setText(item.getTitle());
                            country = item.getTitle().toString();
                            regionId = String.valueOf(item.getItemId());
                            return true;
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<regionLodel> call, Throwable t) {

            }
        });
    }

    private void popup() {
        popupMenu2 = new PopupMenu(SignUpActivity.this, spinner_designation);
        popupMenu2.getMenuInflater().inflate(R.menu.designation_menu, popupMenu2.getMenu());
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                spinner_designation.setText(item.getTitle());
                if (item.getTitle().equals("Captain")){
                    destId = "0";
                }else if (item.getTitle().equals("First Officer")){
                    destId = "1";
                }else if (item.getTitle().equals("Student pilot")){
                    destId = "2";
                }else if (item.getTitle().equals("Engineer")){
                    destId = "3";
                }else if (item.getTitle().equals("Cabin Crew")){
                    destId = "4";
                }else if (item.getTitle().equals("Other")){
                    destId = "5";
                }
                return true;
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCallSignUp != null) mCallSignUp.cancel();
    }

    private void initView() {
        mFieldsValidator = new FieldsValidator();
        setHighLightedText(binding.textLogin, getString(R.string.already_have_an_account_login), 25, 31);
        setBaseCallback(baseCallback);
//        if (getIntent().getExtras() != null) {
//            mPhoneNumber = getIntent().getStringExtra("number");
//            if (mPhoneNumber != null)
//                binding.editTextMobileNumber.setText(String.format("+91 %s", mPhoneNumber));
//        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_designation, R.layout.item_simple_spinner);
        adapter.setDropDownViewResource(R.layout.item_simple_spinner_dropdown);
//        binding.spinnerDesignation.setAdapter(adapter);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
//            case R.id.image_eye_create_pass:
//                showHidePassCreatePass();
//                break;
//            case R.id.image_eye_confirm_pass:
//                showHidePassConfirmPass();
//                break;
            case R.id.button_register:
                if (validate()) signUpApi();
                break;
            case R.id.back_signup:
                startActivity(new Intent(SignUpActivity.this, LoginNewActivity.class));
                goNextAnimation();
                finish();
                break;
            case R.id.region:
                popupMenu.show();
                break;

            case R.id.spinner_designation:
                popupMenu2.show();
                break;
         case R.id.date_of_birth:
             datePickClicked();
                break;
        }
    };

    public void setHighLightedText(TextView tv, String text, int start, int end) {
        Spannable wordToSpan = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
                startActivity(new Intent(SignUpActivity.this, LoginNewActivity.class));
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                finishAffinity();
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

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private boolean validate() {

        if (binding.editTextFirstName.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter first name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.editTextLastName.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter last name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (binding.editTextEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!binding.editTextEmail.getText().toString().matches(emailPattern)){
            Toast.makeText(this, "Please enter email with correct format.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (destId.equals("")){
            Toast.makeText(this, "Please choose designation", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (regionId.equals("")){
            Toast.makeText(this, "Please choose region", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;

       /* return mFieldsValidator.hasText(binding.editTextFirstName) &&
                mFieldsValidator.hasText(binding.editTextLastName) &&
                mFieldsValidator.isEmailAddress(binding.editTextEmail, true) &&
                mFieldsValidator.hasText(binding.editTextEmail)&&mFieldsValidator.hasText(binding.editTextNumber);

//                &&
//                mFieldsValidator.hasText(binding.editTextCreatePass);*/
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void signUpApi() {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

       /* Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(binding.dateOfBirth.getText().toString());
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
       // Log.d("QWERTYUIO",str);
        if (email == null || email.equalsIgnoreCase("")){
            email = binding.editTextEmail.getText().toString().trim();
        }else {

        }
        if (num.equalsIgnoreCase("")){
            num = binding.editTextNumber.getText().toString().trim();
        }else {

        }
        mMapValues = new HashMap<>();
        showBaseProgress();
        mMapValues.put("first_name", binding.editTextFirstName.getText().toString());
        mMapValues.put("email", email);
        mMapValues.put("last_name", binding.editTextLastName.getText().toString());

        if (mFireBaseToken != null) {
            mMapValues.put("device_token", mFireBaseToken);
        } else {
            hideBaseProgress();
            getToken();
            return;
        }
        mMapValues.put("phone_number", num);
        mMapValues.put("country_code", countryCodeNum);
        mMapValues.put("region_id", regionId);
        mMapValues.put("country", country);
        mMapValues.put("device_type", Constants.DEVICETYPE);
      //  mMapValues.put("dob",str);
        mMapValues.put("atpl_number", binding.editTextCplNumber.getText().toString());
//        mMapValues.put("atpl_number", binding.editTextAtplNumber.getText().toString());
        mMapValues.put("designation", String.valueOf(destId));

        mCallSignUp = AppController.getInstance().getApis().signUp(mMapValues);
        mCallSignUp.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel loginBean) {

                if (loginBean != null) {
                    if (loginBean.getStatus() == Constants.SUCCESS_CODE) {
                        //showToast(loginBean.getMessage());
                        showToast("Account has been created successfully..");
                        if (PrefUtils.getInstance().setUser(loginBean.getData())) {
                                MySharedPreferences.getInstance().saveString(SignUpActivity.this, ConstantsNew.EMAIL,loginBean.getData().getEmail());
                                MySharedPreferences.getInstance().saveString(SignUpActivity.this, ConstantsNew.NUMBER,loginBean.getData().getPhone_number());
                                MySharedPreferences.getInstance().saveString(SignUpActivity.this, ConstantsNew.FIRST_NAME, loginBean.getData().getFirst_name()+" "+loginBean.getData().getLast_name());
                                if (loginBean != null) {
                                if (loginBean.getStatus() == Constants.SUCCESS_CODE) {
                                    if (PrefUtils.getInstance().setUser(loginBean.getData())) {
                                        goToHomeScreen();
                                    }
                                }
                                }

                        }
                        goNextAnimation();
                        finishAffinity();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) {
                    if (t.getMessage().equalsIgnoreCase("Email already linked with another account.")){
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                SignUpActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Email already linked with another account. Please confirm email again");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();
                               clearText();
                            }
                        });
                        alertDialog.show();

                    }else{
                        showErrorToast(t.getMessage());
                    }


                }

            }

            @Override
            public void onComplete(Call<LoginModel> call, Throwable t) {
                hideBaseProgress();
                onCallComplete(call, t);

            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getToken();
    }

    private void checkEmailAvailability(String email) {
        mMapValues = new HashMap<>();
        mMapValues.put("email", email);
        mCallCheckEmailAvail = AppController.getInstance().getApis().checkEmailAvailability(email);
        mCallCheckEmailAvail.enqueue(new ResponseHandler<SignUpBean>() {
            @Override
            public void onSuccess(SignUpBean loginBean) {

                if (loginBean != null) {
                    if (loginBean.getStatus() == Constants.SUCCESS_CODE) {
                        //  showSuccessToast(loginBean.getMessage());
                        binding.buttonRegister.setEnabled(true);
                    } else {
                        showErrorToast(loginBean.getMessage());
                        binding.buttonRegister.setEnabled(false);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                if (t != null) showErrorToast(t.getMessage());

            }

            @Override
            public void onComplete(Call<SignUpBean> call, Throwable t) {

                onCallComplete(call, t);

            }


        });
    }

    private void
    getToken() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                mFireBaseToken = instanceIdResult.getToken();

            }
        });
    }

    public final DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    showDate();
                }
            };

    public void showDate() {
        binding.dateOfBirth.setText(sdf.format(calendar.getTime()));
    }

    public void datePickClicked() {
        DatePickerDialog dp;
        if (!dob.equalsIgnoreCase("")) {
            dp = new DatePickerDialog(this, R.style.DialogTheme, myDateListener, year, month - 1, day);

        } else {
            dp = new DatePickerDialog(this, R.style.DialogTheme, myDateListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }
        dp.getDatePicker().setMaxDate(System.currentTimeMillis());
//        dp.updateDate(2016, 5, 22);
        dp.show();
    }

    private void clearText(){
        email="";
    }

}
