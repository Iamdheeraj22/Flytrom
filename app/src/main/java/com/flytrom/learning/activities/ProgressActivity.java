package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.ChangePasswordActivity;
import com.flytrom.learning.activities.others.ResetActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.adapters.ProgressAdapter;
import com.flytrom.learning.adapters.VideoCategoryAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.databinding.ActivityProgressBinding;
import com.flytrom.learning.databinding.ActivitySettingsBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.progressModel.ProgressData;
import com.flytrom.learning.model.progressModel.ProgressModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgressActivity extends BaseActivity<ActivityProgressBinding> {
    Apis apisInterface;
    ProgressAdapter progressAdapter;
    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apisInterface = AppController.getInstance().getApis();
        binding.personName.setText(MySharedPreferences.getInstance().getString(ProgressActivity.this, ConstantsNew.FIRST_NAME));
        binding.toolbar.textTitle.setText("Progress");
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);

        calProfressApi();
    }

    private void calProfressApi() {
        apisInterface.getProgress(getHeader()).enqueue(new Callback<ProgressModel>() {
            @Override
            public void onResponse(Call<ProgressModel> call, Response<ProgressModel> response) {
                if (response.isSuccessful()){
                    ProgressModel progressModel = response.body();
                    callAdapter(progressModel.getData());
                }else {
                    Log.d("JGGY",response.message());
                }

            }

            @Override
            public void onFailure(Call<ProgressModel> call, Throwable t) {
                Log.d("VBHJJKJ",t.getLocalizedMessage());
            }
        });
    }

    private void callAdapter(List<ProgressData> data) {
        progressAdapter = new ProgressAdapter(ProgressActivity.this,data);
        binding.RVProgress.setHasFixedSize(true);
        binding.RVProgress.setLayoutManager(new LinearLayoutManager(this));
        binding.RVProgress.setAdapter(progressAdapter);
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_progress;
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
         case R.id.mScb:
                Intent intent = new Intent(ProgressActivity.this, SubscribeActivity.class);
                MySharedPreferences.getInstance().saveString(ProgressActivity.this, ConstantsNew.OWNER_ID,"1");
                startActivity(intent);
                break;
        }
    };

}