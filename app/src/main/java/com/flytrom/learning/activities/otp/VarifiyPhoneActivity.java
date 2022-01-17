package com.flytrom.learning.activities.otp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.SendOtpPojo;
import com.flytrom.learning.utils.AppController;
import com.hbb20.CountryCodePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VarifiyPhoneActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    String countryCode,number,fullNumber;
    Apis apiInterface;
    EditText edit_text_mobile_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varifiy_phone);
        apiInterface = AppController.getInstance().getApisOtp();

        edit_text_mobile_number = findViewById(R.id.edit_text_mobile_number);
        ccp = findViewById(R.id.country_code_pickerOtp);
        countryCode = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = ccp.getSelectedCountryCodeWithPlus();

            }
        });
    }

    public void clickOtp(View view) {

        number = edit_text_mobile_number.getText().toString();
        fullNumber = countryCode+number;
        Intent intent = new Intent(VarifiyPhoneActivity.this, OtpActivity.class);
        intent.putExtra("otpNum",fullNumber);
        startActivity(intent);


    }
}