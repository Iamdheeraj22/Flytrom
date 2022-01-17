package com.flytrom.learning.activities.info;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.ChangePasswordActivity;
import com.flytrom.learning.activities.others.ResetActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.adapters.RegionAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.databinding.ActivitySettingsBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.DataRegion;
import com.flytrom.learning.model.EmailOtp;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.model.regionLodel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppSettingsActivity extends BaseActivity<ActivitySettingsBinding> implements RegionAdapter.OnItemClickListner {
    Apis apisInterface;
    RegionAdapter regionAdapter;
    RecyclerView regionCatogery;
    CardView cardResgin;
    String id;
    private Call<LoginModel> mGetProfile;
    List<regionLodel> regionLodelsList = new ArrayList<>();

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apisInterface = AppController.getInstance().getApis();
        getProfile();
        regionCatogery = findViewById(R.id.regionCatogery);
        cardResgin = findViewById(R.id.cardResgin);
        callRegionApi();
        initView();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    private void initView() {

        binding.toolbar.textTitle.setText(getString(R.string.title_app_settings));
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);
        binding.switchVibration.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {
                PrefUtils.getInstance().setVibrationMode(true);
                binding.textVibration.setText("Vibration : ON");
            } else {
                PrefUtils.getInstance().setVibrationMode(false);
                binding.textVibration.setText("Vibration : OFF");
            }
        });

        if (PrefUtils.getInstance().isVibrationModeOn()) binding.switchVibration.setChecked(true);
        else binding.switchVibration.setChecked(false);

    }

    private void callRegionApi() {
        regionLodelsList = new ArrayList<>();
        apisInterface.getRegions().enqueue(new Callback<regionLodel>() {
            @Override
            public void onResponse(Call<regionLodel> call, Response<regionLodel> response) {
                if (response.isSuccessful()){
                    regionLodel RegionLodel = response.body();
                    callAdapter(RegionLodel.getData());

                }
            }

            @Override
            public void onFailure(Call<regionLodel> call, Throwable t) {

            }
        });
    }

    private void callAdapter(List<DataRegion> regionLodel) {
        regionAdapter = new RegionAdapter(regionLodel,AppSettingsActivity.this,this::onitemClickNew);
        regionCatogery.setHasFixedSize(true);
        regionCatogery.setLayoutManager(new LinearLayoutManager(this));
        regionCatogery.setAdapter(regionAdapter);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.text_change_pass:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                goNextAnimation();
                break;
            case R.id.text_reset:
                startActivity(new Intent(this, ResetActivity.class));
                goNextAnimation();
                break;
           case R.id.regionChangeCard:

               cardResgin.setVisibility(cardResgin.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
        }
    };


    @Override
    public void onitemClickNew(String click, int position, String id) {
        cardResgin.setVisibility(View.GONE);
        showDiloge(id,click);

    }

    private void showDiloge(String s, String id) {
        LayoutInflater inflater = LayoutInflater.from(AppSettingsActivity.this);
        View view = inflater.inflate(R.layout.reagon_alert_dialog, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                .setView(view)
                .create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Button acceptButton = view.findViewById(R.id.acceptButtonLogout);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                callUpdateRegionApi(id,s);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();


            }
        });

        alertDialog.show();
    }

    private void callUpdateRegionApi(String s, String id) {
        apisInterface.updateRegion(getHeader(),id).enqueue(new Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                if (response.isSuccessful()){
                    binding.region.setText("Region - "+s);

                }else {
                    Toast.makeText(AppSettingsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                Toast.makeText(AppSettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getProfile() {
      apisInterface.getProfile(getHeader()).enqueue(new Callback<LoginModel>() {
          @Override
          public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
              LoginModel loginModel = response.body();
              if (response.isSuccessful()){
                  binding.region.setText("Region - "+loginModel.getData().getRegion_name());
//                  Toast.makeText(AppSettingsActivity.this, "Success", Toast.LENGTH_SHORT).show();
              }else {
                  Toast.makeText(AppSettingsActivity.this, response.message(), Toast.LENGTH_SHORT).show();

              }
          }

          @Override
          public void onFailure(Call<LoginModel> call, Throwable t) {
              Toast.makeText(AppSettingsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
          }
      });
    }

}
