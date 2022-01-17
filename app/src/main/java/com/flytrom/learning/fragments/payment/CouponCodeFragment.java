package com.flytrom.learning.fragments.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.CouponsActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.activities.q_bank_menu.ChaptersActivity;
import com.flytrom.learning.activities.q_bank_menu.CreateCustomModuleActivity;
import com.flytrom.learning.activities.q_bank_menu.GoingToStartModuleActivity;
import com.flytrom.learning.adapters.CouponsAdapter;
import com.flytrom.learning.adapters.QBankCategoryAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CheckCouponBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.databinding.FragmentCouponCodeBinding;
import com.flytrom.learning.databinding.FragmentQBankCategoryBinding;
import com.flytrom.learning.fragments.payment.PurchasePlanFragment;
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


public class CouponCodeFragment extends BaseFragment<FragmentCouponCodeBinding> implements CouponsAdapter.OnItemClickListnerCoupon  {
    CouponsAdapter couponsAdapter;
    String membership_id,codeCpn;
    Apis apisInterface;
    private PlanDataBean mPlanDataBean;
    private Call<CheckCouponBean> mCallCheckCoupon;


    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        apisInterface = AppController.getInstance().getApis();
        SubscribeActivity.mInstance.setToolBarHide("0");
        Bundle args = getArguments();
        if (args  != null){
            membership_id = args.getString("membership_id");
        }
        if (getArguments() != null) {
            mPlanDataBean = (PlanDataBean) getArguments().getSerializable("bean");
        }

        callCouponsApi();

        setBaseCallback(callback);
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
        couponsAdapter = new CouponsAdapter(getActivity(),couponPojo,this);
        binding.recyclerCoupon.setHasFixedSize(true);
        binding.recyclerCoupon.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerCoupon.setAdapter(couponsAdapter);
    }



    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_coupon_code;
    }

    private BaseCallback callback = view -> {
        switch (view.getId()) {
            case R.id.applyCopn:
                codeCpn = binding.couponCodeET.getText().toString().trim();
                checkCouponApi(codeCpn);
                SubscribeActivity.mInstance.setToolBarHide("1");

                break;
            case R.id.image_backCopn:
                Fragment fr= new PurchasePlanFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putSerializable("bean", mPlanDataBean);
                fr.setArguments(args);
                ft.replace(R.id.container_subscribe, fr);
                ft.commit();
                SubscribeActivity.mInstance.setToolBarHide("1");
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
                            Fragment fr= new PurchasePlanFragment();
                            FragmentManager fm=getFragmentManager();
                            FragmentTransaction ft=fm.beginTransaction();
                            Bundle args = new Bundle();
                            args.putSerializable("bean", mPlanDataBean);
                            args.putSerializable("beanData",checkCouponBean.getData());
                            args.putString("couponCode",checkCouponBean.getData().getCode());
                            fr.setArguments(args);
                            ft.replace(R.id.container_subscribe, fr);
                            ft.commit();
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
                    Toast.makeText(getActivity(), "Invalid Coupon Code", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}