package com.flytrom.learning.activities.info;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.others.EditProfileActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.LoginBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.databinding.ActivityProfileBinding;
import com.flytrom.learning.databinding.DialogLogoutBinding;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.CircleImageTransform;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;

public class ProfileActivity extends BaseActivity<ActivityProfileBinding> {

    private Call<LoginModel> mCallLogout;
    private BaseCustomDialog<DialogLogoutBinding> mDialogLogout;
    private Call<LoginModel> mGetProfile;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_profile;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
        initView();
    }

    @Override
    protected void onDestroy() {
        if (mCallLogout != null) mCallLogout.cancel();
        super.onDestroy();
    }

    private void initView() {
        getProfile();
        binding.toolbar.textTitle.setText("My Account");
        binding.toolbar.imageBack.setColorFilter(this.getResources().getColor(R.color.white));
        binding.toolbar.toolRL.setBackgroundColor(Color.parseColor("#4E7ADA"));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#ffffff"));
        binding.toolbar.editText.setTextColor(Color.parseColor("#ffffff"));
        binding.toolbar.relativeEdit.setVisibility(View.VISIBLE);
        setBaseCallback(baseCallback);
        setUserData();


    }

    private void setUserData() {
        UserDataBean userBean = PrefUtils.getInstance().getUser();
        if (userBean != null) {
            if (userBean.getProfile_picture() != null)
                Picasso.with(ProfileActivity.this).load(Constants.MEDIA_URL + userBean.getProfile_picture())
                        .transform(new CircleImageTransform())
                        .placeholder(R.drawable.ic_user_placeholder)
                        .into(binding.imageProfile);
            binding.editTextName.setText(userBean.getFirst_name().toUpperCase() + " " + userBean.getLast_name().toUpperCase());
            binding.textEmail.setText(userBean.getEmail());
            binding.textPhoneNumber.setText(String.format("("+"+"+userBean.getCountry_code().toString()+")"+userBean.getPhone_number()));
            binding.reagion.setText(userBean.getRegion_name());
           // parseDateToddMMyyyy(userBean.getDob());
            binding.editTextCpl.setText("CPL Number - " + userBean.getCpl_number());
            binding.editTextAtpl.setText( userBean.getAtpl_number());
            binding.textDesignation.setText(Constants.DesignationNames[Integer.parseInt(userBean.getDesignation())].toUpperCase());
        }
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-mm-dd";
        String outputPattern = "dd-mm-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.dateofbirth.setText(str);
        return str;
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.text_my_subs:
                goToSubscribeScreen();
                break;
            case R.id.relative_logout:
                dialogLogout();
                break;
            case R.id.relative_edit:
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
                goNextAnimation();
                break;
         case R.id.llRegion:
             startActivity(new Intent(ProfileActivity.this, AppSettingsActivity.class));
             finish();
             goNextAnimation();
             break;
        }
    };

    private void goToSubscribeScreen() {
        startActivity(new Intent(ProfileActivity.this, SubscribeActivity.class)
                .putExtra("from", "my_subs"));
        goNextAnimation();
    }

    private void dialogLogout() {
        mDialogLogout = new BaseCustomDialog<>(this, R.layout.dialog_logout, view -> {
            mDialogLogout.dismiss();
            logoutUserApi();
        });
        Objects.requireNonNull(mDialogLogout.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialogLogout.setCancelable(true);
        mDialogLogout.show();
    }

    private void logoutUserApi() {
        showBaseProgress();
        mCallLogout = AppController.getInstance().getApis().logOut(getHeader());
        mCallLogout.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        mDBRepositoryKotlin.clearRoomDatabase();
                        logoutUser();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null)
                    showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_logout, Throwable t) {
                onCallComplete(call_logout, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void getProfile() {
        mGetProfile = AppController.getInstance().getApis().getProfile(getHeader());
        mGetProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {
                Log.e("onSuccess",model.getMessage());
                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        PrefUtils.getInstance().setUser(model.getData());
                        setUserData();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean apiError) {
                //if (t != null) showErrorToast(t.getMessage());
                Log.e("apiError",apiError.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                //onCallComplete(call_update_profile, t);
//                Log.e("onComplete",t.getMessage());
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
}
