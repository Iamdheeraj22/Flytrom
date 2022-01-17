package com.flytrom.learning.activities.changeNumEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.utils.AppController;
import com.hbb20.CountryCodePicker;

import info.jeovani.viewpagerindicator.ViewPagerIndicator;

public class ChangePhoneOrrEmailActivity extends AppCompatActivity {

    TextView signupText,changeText;
    private LinearLayout llPhone,llEmail;
    private EditText edit_text_mobile_number,edit_text_email;
    private CountryCodePicker ccp;
    private String countryCode,country,countryNumCode;
    private String number,fullNumber,email;
    ImageView back;
    Apis apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_orr_email);

        llPhone = findViewById(R.id.llPhone2);
        changeText = findViewById(R.id.changeText);
        back = findViewById(R.id.back);
        llEmail = findViewById(R.id.llEmail2);
        signupText = findViewById(R.id.signupText2);
        edit_text_email = findViewById(R.id.edit_text_email2);
        apiInterface = AppController.getInstance().getApis();

        ccp = findViewById(R.id.country_code_picker2);
        countryCode=ccp.getSelectedCountryCodeWithPlus();
        country = ccp.getSelectedCountryName();
        edit_text_mobile_number = findViewById(R.id.edit_text_mobile_number2);
        countryNumCode = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = ccp.getSelectedCountryCodeWithPlus();
                country = ccp.getSelectedCountryName();
                countryNumCode = ccp.getSelectedCountryCodeWithPlus();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void changeInsted(View view) {
        if (signupText.getText().equals("Change Phone instead")){
            llEmail.setVisibility(View.GONE);
            llPhone.setVisibility(View.VISIBLE);
            changeText.setText("Change Mobile Number");
            signupText.setText("Change Email instead");
        }else if (signupText.getText().equals("Change Email instead")){
            llEmail.setVisibility(View.VISIBLE);
            changeText.setText("Change Email");
            llPhone.setVisibility(View.GONE);
            signupText.setText("Change Phone instead");
        }
    }

    public void clickOtpChange(View view) {
        number = edit_text_mobile_number.getText().toString();
        email = edit_text_email.getText().toString();
        fullNumber = countryCode+number;

        if (number.equalsIgnoreCase("")){
        }else {
            Intent intent = new Intent(ChangePhoneOrrEmailActivity.this,ChangeOtpActivity.class);
            intent.putExtra("otpNum",fullNumber);
            intent.putExtra("country",country);
            intent.putExtra("countryCodeNum",countryNumCode);
            intent.putExtra("number",number);
            intent.putExtra("countryCode",countryCode);
            startActivity(intent);
            finish();
        }

        if (email.equalsIgnoreCase("")){

        }else {
            Intent intent2 = new Intent(ChangePhoneOrrEmailActivity.this,ChangeOtpActivity.class);
            intent2.putExtra("email",email);
            startActivity(intent2);
            finish();
        }

    }
}