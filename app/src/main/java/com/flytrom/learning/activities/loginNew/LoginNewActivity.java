package com.flytrom.learning.activities.loginNew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.SignUpActivity;
import com.flytrom.learning.activities.auth.ValidatePhoneActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.activities.others.SplashActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.activities.otp.VarifiyPhoneActivity;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.hbb20.CountryCodePicker;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import info.jeovani.viewpagerindicator.ViewPagerIndicator;
import info.jeovani.viewpagerindicator.constants.PagerItemType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNewActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    CountryCodePicker ccp;
    String countryCode,country,countryNumCode;
    ViewPagerIndicator mainViewPagerIndicator;
    String number,fullNumber,email;
    Apis apiInterface;
    TextView signupText;
    LinearLayout llPhone,llEmail,loginLL,constraintLayout;
    EditText edit_text_mobile_number,edit_text_email;
    private ImageButton btnNext;
//    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        viewPager =  findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);
        mainViewPagerIndicator = findViewById(R.id.mViewPagerIndicator);
        llPhone = findViewById(R.id.llPhone);
        llEmail = findViewById(R.id.llEmail);
        signupText = findViewById(R.id.signupText);
        edit_text_email = findViewById(R.id.edit_text_email);
        loginLL = findViewById(R.id.loginLL);
        final ScrollView scrollTuch = findViewById(R.id.scrollTuchLogin);
        apiInterface = AppController.getInstance().getApis();
         // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.slide1,
                R.layout.slide2,
                R.layout.slide3};

        // adding bottom dots
//        addBottomDots(0);

        // making notification bar transparent
        changeStatusBarColor();

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        edit_text_mobile_number = findViewById(R.id.edit_text_mobile_number);
        ccp = findViewById(R.id.country_code_picker);
        countryCode=ccp.getSelectedCountryCodeWithPlus();
        country = ccp.getSelectedCountryName();
        countryNumCode = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = ccp.getSelectedCountryCodeWithPlus();
                country = ccp.getSelectedCountryName();
                countryNumCode = ccp.getSelectedCountryCodeWithPlus();
            }
        });



        mainViewPagerIndicator.setItemsCount(3);
 //        mainViewPagerIndicator.itemSelectedColors = arrayListOf(Color.parseColor("#bf360c"), Color.parseColor("#ff7043"))
//        mainViewPagerIndicator.itemsUnselectedColors = arrayListOf(Color.parseColor("#cfd8dc"), Color.parseColor("#90a4ae"))
         mainViewPagerIndicator.setItemWidth(30);
        mainViewPagerIndicator.setItemHeight(6);
        mainViewPagerIndicator.setBackgroundColor(Color.TRANSPARENT);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);

//        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
//
//            @Override
//            public void onVisibilityChanged(boolean isOpen) {
//
//                if (isOpen){
//                    Toast.makeText(LoginNewActivity.this, "keyboard opened",Toast.LENGTH_SHORT).show();
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                    );
//                    params.setMargins(0, 0, 0, 200);
//                    loginLL.setLayoutParams(params);
//                } else{
//                    Toast.makeText(LoginNewActivity.this, "keyboard hidden", Toast.LENGTH_SHORT).show();
//                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT
//                    );
//                    params.setMargins(0, 0, 0, 10);
//                    loginLL.setLayoutParams(params);
//                }
//
//            }
//        });
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageSelected(int position) {
//            addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void clickSignup(View view) {

        if (signupText.getText().equals("Login/signup with Phone instead")){
            llEmail.setVisibility(View.GONE);
            edit_text_email.setText("");
            llPhone.setVisibility(View.VISIBLE);
            signupText.setText("Login/signup with Email instead");
        }else if (signupText.getText().equals("Login/signup with Email instead")){
            llEmail.setVisibility(View.VISIBLE);
            llPhone.setVisibility(View.GONE);
            edit_text_mobile_number.setText("");
            signupText.setText("Login/signup with Phone instead");
        }

    }


    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public void clickOtp(View view) {

        number = edit_text_mobile_number.getText().toString();
        email = edit_text_email.getText().toString();
        fullNumber = countryCode+number;

        if (number.equalsIgnoreCase("")){
        }else {
            Intent intent = new Intent(LoginNewActivity.this, OtpActivity.class);
            intent.putExtra("otpNum",fullNumber);
            intent.putExtra("country",country);
            intent.putExtra("countryCodeNum",countryNumCode);
            intent.putExtra("number",number);
            intent.putExtra("countryCode",countryCode);
            startActivity(intent);
        }

        if (email.equalsIgnoreCase("")){
        }else {
            Intent intent3 = new Intent(LoginNewActivity.this, OtpActivity.class);
            intent3.putExtra("email",email);
            startActivity(intent3);
//            loginWithEmailId();

        }
    }

    private void loginWithEmailId() {
        apiInterface.loginWithEmail(email, Constants.DEVICETYPE,"abc").enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {

                LoginModel loginModel = response.body();
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 202){
                        Intent intent2 = new Intent(LoginNewActivity.this, OtpActivity.class);
                        intent2.putExtra("email",email);
                        startActivity(intent2);
                    }else if (response.body().getStatus() == 200){
                        Intent intent3 = new Intent(LoginNewActivity.this, OtpActivity.class);
                        intent3.putExtra("email",email);
                        startActivity(intent3);
                        if (PrefUtils.getInstance().setUser(loginModel.getData())) {
                            goToHomeScreen();
                        }


                    }
                }else {
                    Toast.makeText(LoginNewActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(LoginNewActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToHomeScreen() {
        if (PrefUtils.getInstance().getUser() != null) {

            UserDataBean loginBean = PrefUtils.getInstance().getUser();

            Intent intent;

            if (loginBean != null) {
                intent = new Intent(LoginNewActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }
    }


}