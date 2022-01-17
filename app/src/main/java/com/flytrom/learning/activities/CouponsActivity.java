package com.flytrom.learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.adapters.CouponsAdapter;
import com.flytrom.learning.adapters.ProgressAdapter;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CheckCouponBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CouponAppliedBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GetPlansBean;
import com.flytrom.learning.databinding.ActivityCouponsBinding;
import com.flytrom.learning.databinding.ActivityProgressBinding;
import com.flytrom.learning.databinding.DialogEnterCouponBinding;
import com.flytrom.learning.interfaces.Apis;
import com.flytrom.learning.model.CouponModel.CouponData;
import com.flytrom.learning.model.CouponModel.CouponPojo;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponsActivity extends BaseActivity<ActivityCouponsBinding> implements CouponsAdapter.OnItemClickListnerCoupon {
    CouponsAdapter couponsAdapter;
    String membership_id;
    Apis apisInterface;
    EditText couponCodeET;
    CardView applyCopn;
    private Call<CheckCouponBean> mCallCheckCoupon;
    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_coupons;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        apisInterface = AppController.getInstance().getApis();
        applyCopn = findViewById(R.id.applyCopn);
        couponCodeET = findViewById(R.id.couponCodeET);
        Intent intent1 = getIntent();
        membership_id = intent1.getStringExtra("membership_id");
        if (intent != null){

        }
        callCouponsApi();
        binding.toolbar.textTitle.setText("Progress");
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
        setBaseCallback(baseCallback);


    }

    private void callCouponsApi() {
        apisInterface.getPlansCoupon(getHeader(),"1").enqueue(new Callback<CouponPojo>() {
            @Override
            public void onResponse(Call<CouponPojo> call, Response<CouponPojo> response) {
                CouponPojo couponPojo = response.body();
                callAdapter(couponPojo.getData());
            }

            @Override
            public void onFailure(Call<CouponPojo> call, Throwable t) {

            }
        });
    }

    private void callAdapter(List<CouponData> couponPojo) {
        couponsAdapter = new CouponsAdapter(CouponsActivity.this,couponPojo,this);
        binding.recyclerCoupon.setHasFixedSize(true);
        binding.recyclerCoupon.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerCoupon.setAdapter(couponsAdapter);
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
        }
    };


    @Override
    public void onitemClickCoupon(String code, String type, String discount) {
        checkCouponApi(code);
    }

    private void checkCouponApi(String coupon) {
        if (mCallCheckCoupon != null) mCallCheckCoupon.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("membership_id", membership_id);
        map.put("coupon", coupon);
        mCallCheckCoupon = AppController.getInstance().getApis().checkCoupon(getHeader(), map);
        mCallCheckCoupon.enqueue(new ResponseHandler<CheckCouponBean>() {
            @Override
            public void onSuccess(CheckCouponBean checkCouponBean) {

                if (checkCouponBean != null) {
                    if (checkCouponBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(checkCouponBean.getMessage());
                        if (checkCouponBean.getMessage().equals("Coupon Applied")) {
//                            discountUser(checkCouponBean.getData());
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {

                // Log.d("t_message", t.getMessage());
            }

            @Override
            public void onComplete(Call<CheckCouponBean> call, Throwable t) {

                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == 409) {
                    Toast.makeText(CouponsActivity.this, "Invalid Coupon Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}