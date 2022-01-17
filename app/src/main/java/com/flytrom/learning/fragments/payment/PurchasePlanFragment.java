 package com.flytrom.learning.fragments.payment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.ProgressActivity;
import com.flytrom.learning.activities.otp.OtpActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseCustomDialog;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.PurchasedPlansBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.beans.response_beans.others.SuccessBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CheckCouponBean;
import com.flytrom.learning.beans.response_beans.payment_beans.CouponBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GenerateTokenBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GetNewOrderIdBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.beans.response_beans.payment_beans.TokenBean;
import com.flytrom.learning.databinding.DialogEnterCouponBinding;
import com.flytrom.learning.databinding.FragmentPurchasePlanBinding;
import com.flytrom.learning.databinding.ItemPurchanPlanSubjectBinding;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.BackStackManager;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;
import com.gocashfree.cashfreesdk.CFPaymentService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import timber.log.Timber;

import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_APP_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_ID;
import static com.gocashfree.cashfreesdk.CFPaymentService.PARAM_ORDER_NOTE;

public class PurchasePlanFragment extends BaseFragment<FragmentPurchasePlanBinding> {

    private String mNewOrderId;
    private String couponCodes;
    String totalDiscount = "0";
    private Call<GetNewOrderIdBean> mCallGetNewOrderId;
    private Call<GenerateTokenBean> mCallGenerateToken;
    private Call<SuccessBean> mCallMakePayment;
    private PlanDataBean mPlanDataBean;
    private CouponBean couponBean;
    private SimpleRecyclerViewAdapter<String, ItemPurchanPlanSubjectBinding> mSubjectAdapter;
    private BaseCustomDialog<DialogEnterCouponBinding> mEnterCouponDialog;
    private Call<CheckCouponBean> mCallCheckCoupon;
    private Call<LoginModel> mCallGetProfile;
    String ammount = "";
    String validity;

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        SubscribeActivity.mInstance.setToolBarTitle("Buy Plan");
        SubscribeActivity.mInstance.setToolBarHide("1");
        binding.couponDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponCodes = "";
                binding.couponDis.setVisibility(View.GONE);
                binding.imageEditCoupon.setVisibility(View.VISIBLE);
                binding.subTotal.setText(ammount);
                binding.textBuy2.setText("Pay - \u20B9 "+ammount);
            }
        });
        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_purchase_plan;
    }

    @Override
    public void onDetach() {
        if (mCallGetNewOrderId != null) mCallGetNewOrderId.cancel();
        if (mCallGenerateToken != null) mCallGenerateToken.cancel();
        if (mCallCheckCoupon != null) mCallCheckCoupon.cancel();
        super.onDetach();
    }

    public void activityResult(Intent data) {

        if (MySharedPreferences.getInstance().getString(getActivity(), ConstantsNew.TOTAL_DISCOUNT).equalsIgnoreCase("")){
            totalDiscount = "0";
        }else {
            totalDiscount = MySharedPreferences.getInstance().getString(getActivity(), ConstantsNew.TOTAL_DISCOUNT);
        }
        //Same request code for all payment APIs.
        //Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Timber.d("API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                if (bundle.getString("txMsg") != null)
                    showToast(bundle.getString("txMsg"));

                if (bundle.getString("txStatus") != null) {
                    if (Objects.equals(bundle.getString("txStatus"), "SUCCESS")) {
                        showSnack(SubscribeActivity.mInstance.getLinearLayoutParent(), getString(R.string.text_please_wait_to_update_details));
                        makePaymentApi(bundle);
                    }
                }
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Timber.d(key + " : " + bundle.getString(key));
                    }
                }
            } else showToast("Payment Failed");
        }
    }

    private void initView() {
        getProfile();
        setBaseCallback(baseCallback);
        if (getArguments() != null) {
            mPlanDataBean = (PlanDataBean) getArguments().getSerializable("bean");
            couponBean = (CouponBean) getArguments().getSerializable("beanData");
            couponCodes = getArguments().getString("couponCode");
            if (mPlanDataBean != null) {
                setData(mPlanDataBean);
                getNewOrderId();
            }

            if (couponBean != null){
                discountUser(couponBean);
            }
        }
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_edit_coupon:

                Fragment fr= new CouponCodeFragment();
                FragmentManager fm=getFragmentManager();
                FragmentTransaction ft=fm.beginTransaction();
                Bundle args = new Bundle();
                args.putString("membership_id",String.valueOf(mPlanDataBean.getId()) );
                args.putSerializable("bean", mPlanDataBean);
                fr.setArguments(args);
                ft.replace(R.id.container_subscribe, fr);
                ft.addToBackStack(null);
                ft.commit();
//                Bundle args = new Bundle();
//                args.putString("membership_id",String.valueOf(mPlanDataBean.getId()) );
//                args.putSerializable("bean", mPlanDataBean);
//                CouponCodeFragment fragment = new CouponCodeFragment();
//                fragment.setArguments(args);
//                BackStackManager.getInstance(getActivity()).pushSubFragments(R.id.container_subscribe, SubscribeActivity.TAG, fragment, true);
                break;
            case R.id.text_buy2:
                if (mPlanDataBean != null) {
                    UserDataBean dataBean = PrefUtils.getInstance().getUser();
                    if (dataBean != null) {
                        List<PurchasedPlansBean> myPlans = dataBean.getPurchasedPlans();

                        if (myPlans != null){
                            for (int i = 0; i < myPlans.size(); i++) {
                                PurchasedPlansBean purchased = myPlans.get(i);

                                if (mPlanDataBean.getHasMeta().equals("1")) {
                                    if (purchased.getPlanMetadataId().equals(String.valueOf(mPlanDataBean.getPlan_metadata_id()))) {
//                                        showToast(getString(R.string.you_have_already_purchased));
                                        binding.alrtPlan.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                }
//                            else {
//                                if (purchased.getPlanId() == mPlanDataBean.getId()) {
//                                    showToast(getString(R.string.you_have_already_purchased));
//                                    return;
//                                }
//                            }
                            }

                        }
                        generateToken(mPlanDataBean);
                    }
                }
                break;

            case R.id.okDilog:
                binding.alrtPlan.setVisibility(View.GONE);
                break;
        }
    };

    private void showAddCouponDialog() {
        mEnterCouponDialog = new BaseCustomDialog<>(Objects.requireNonNull(getActivity()), R.layout.dialog_enter_coupon, view -> {
            switch (view.getId()) {
                case R.id.text_cancel:
                    mEnterCouponDialog.dismiss();
                    break;
                case R.id.text_apply:
                    if (mEnterCouponDialog.getBinding().editTextCode.getText().length() > 0)
                        checkCouponApi(mEnterCouponDialog, mEnterCouponDialog.getBinding().editTextCode.getText().toString());
                    break;
            }
        });
        Objects.requireNonNull(mEnterCouponDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mEnterCouponDialog.setCancelable(true);
        mEnterCouponDialog.show();
    }

    private void setData(PlanDataBean planDataBean) {

        switch (planDataBean.getPlanType()) {
            case "Plan1":
                binding.qbankPlan2.setVisibility(View.VISIBLE);
                binding.testPlam2.setVisibility(View.VISIBLE);
                binding.videoplan2.setVisibility(View.VISIBLE);
                binding.textType2.setText(getString(R.string.plan_one_title));
                break;
            case "Plan2":
                binding.qbankPlan2.setVisibility(View.VISIBLE);
                binding.testPlam2.setVisibility(View.VISIBLE);
                binding.videoplan2.setVisibility(View.GONE);
                binding.textType2.setText(getString(R.string.plan_two_title));
                break;
            case "Plan3":
                binding.videoplan2.setVisibility(View.VISIBLE);

                binding.textType2.setText(getString(R.string.plan_three_title));
                break;
        }

        if (planDataBean.getSubjects() != null) {
            List<String> items = Arrays.asList(planDataBean.getSubjects().split(","));
            if (items.size() > 0) {
                setSubjects(items);
            }
        }

        binding.textTotal2.setText("\u20B9"+planDataBean.getAmount());
        binding.textAmount2.setText("\u20B9"+planDataBean.getAmount());
        ammount = planDataBean.getAmount();
        binding.subTotal.setText(planDataBean.getAmount());
        binding.textBuy2.setText("Pay - \u20B9 "+planDataBean.getAmount());
        binding.textDuration2.setText(planDataBean.getValidity()+" Months Validity");
        validity = planDataBean.getValidity();
        for (int i = 0; i < planDataBean.getPlanMetadata().size(); i++) {
             binding.titlePlanName.setText(planDataBean.getPlanMetadata().get(i).getTitle()+" "+planDataBean.getPlanMetadata().get(i).getType());

        }


        if (!planDataBean.getOldAmount().equals("0")) {
//            binding.relativeOldPrice.setVisibility(View.VISIBLE);
            binding.textOldAmount2.setText(planDataBean.getOldAmount().toString());
            binding.textOldAmount2.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            binding.textRsOld.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void setSubjects(List<String> items) {
        mSubjectAdapter = new SimpleRecyclerViewAdapter<>(R.layout.item_purchan_plan_subject, BR.subject, (v, s) -> {

        });
        binding.rvSubjects2.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvSubjects2.setAdapter(mSubjectAdapter);
        mSubjectAdapter.setList(items);
    }

    private void getNewOrderId() {
        mCallGetNewOrderId = AppController.getInstance().getApis().getNewOrderId(getHeader());
        mCallGetNewOrderId.enqueue(new ResponseHandler<GetNewOrderIdBean>() {
            @Override
            public void onSuccess(GetNewOrderIdBean getNewOrderIdBean) {

                if (getNewOrderIdBean != null) {
                    if (getNewOrderIdBean.getStatus() == Constants.SUCCESS_CODE) {
                        mNewOrderId = getNewOrderIdBean.getData().getOrderId();
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetNewOrderIdBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

    private void makePaymentApi(Bundle bundle) {
        showBaseProgress();
        if (mPlanDataBean != null) {
            HashMap<String, String> map = new HashMap<>();
            map.put("plan_id", String.valueOf(mPlanDataBean.getId()));
            Log.d("plan","plan_id"+String.valueOf(mPlanDataBean.getId()));
            if (mPlanDataBean.getHasMeta().equals("0"))
                map.put("plan_metadata_id", "0");
            else
                Log.d("plan","plan_metadata_id"+String.valueOf(mPlanDataBean.getPlan_metadata_id()));
                map.put("plan_metadata_id", String.valueOf(mPlanDataBean.getPlan_metadata_id()));

            map.put("referenceId", bundle.getString("referenceId"));
            map.put("paymentMode", bundle.getString("paymentMode"));
            map.put("txStatus", bundle.getString("txStatus"));
            map.put("txTime", bundle.getString("txTime"));
            map.put("validity", validity);
            map.put("total_discount", totalDiscount);

            //Details about the plan
            Log.d("plan","referenceId"+ bundle.getString("referenceId"));
            Log.d("plan","paymentMode"+bundle.getString("paymentMode"));
            Log.d("plan","txStatus"+ bundle.getString("txStatus"));
            Log.d("plan","txTime"+bundle.getString("txTime"));
            Log.d("plan","validity"+String.valueOf(validity));
            Log.d("plan","total_discount"+String.valueOf(totalDiscount));


            mCallMakePayment = AppController.getInstance().getApis().makePaymentApi(getHeader(), map);
            mCallMakePayment.enqueue(new ResponseHandler<SuccessBean>() {
                @Override
                public void onSuccess(SuccessBean successBean) {

                    if (successBean != null) {
                        if (successBean.getStatus() == Constants.SUCCESS_CODE) {
                            Timber.d("dfd");
                            showToast(successBean.getMessage());
                            MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.TOTAL_DISCOUNT, "0");
                            getProfile2();
                            showToast("Plan has been purchased Successfully");
                            getProfile();
                        }
                    }
                }

                @Override
                public void apiError(ApiErrorBean t) {
                    hideBaseProgress();
                    if (t != null) showErrorToast(t.getMessage());
                }

                @Override
                public void onComplete(Call<SuccessBean> call, Throwable t) {
                    onCallComplete(call, t);
                }

                @Override
                public void statusCode(int t) {
                    super.statusCode(t);
                    handleCodes(t);
                }
            });
        }
    }

    private void generateToken(PlanDataBean dataBean) {
        if (dataBean.getAmount() != null) {
            showBaseProgress();
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId", mNewOrderId);
            map.put("orderAmount", binding.subTotal.getText().toString());

            mCallGenerateToken = AppController.getInstance().getApis().generateToken(getHeader(), map);
            mCallGenerateToken.enqueue(new ResponseHandler<GenerateTokenBean>() {
                @Override
                public void onSuccess(GenerateTokenBean generateTokenBean) {

                    if (generateTokenBean != null) {
                        if (generateTokenBean.getStatus() == Constants.SUCCESS_CODE) {
                            Timber.d("dfd");
                            doPayment(dataBean, generateTokenBean.getData());
                        }
                    }
                }

                @Override
                public void apiError(ApiErrorBean t) {
                    hideBaseProgress();
                    if (t != null) showErrorToast(t.getMessage());
                }

                @Override
                public void onComplete(Call<GenerateTokenBean> call, Throwable t) {
                    onCallComplete(call, t);
                }

                @Override
                public void statusCode(int t) {
                    super.statusCode(t);
                    handleCodes(t);
                }
            });
        }
    }

    private void doPayment(PlanDataBean planBean, TokenBean tokenBean) {
        showBaseProgress();
        mPlanDataBean = planBean;

        Map<String, String> params = new HashMap<>();
        params.put(PARAM_APP_ID, Constants.CASHFREE_APP_ID);
        params.put(PARAM_ORDER_ID, mNewOrderId);
        params.put(PARAM_ORDER_AMOUNT, binding.subTotal.getText().toString());
        params.put(PARAM_ORDER_NOTE, planBean.getPlanName());
        params.put(PARAM_CUSTOMER_NAME, PrefUtils.getInstance().getUser().getFirst_name());
        params.put(PARAM_CUSTOMER_PHONE, PrefUtils.getInstance().getUser().getPhone_number());
        params.put(PARAM_CUSTOMER_EMAIL, PrefUtils.getInstance().getUser().getEmail());




        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);


        cfPaymentService.doPayment(getActivity(), params, tokenBean.getCftoken(), Constants.CASH_FREE_TRANSACTION_STAGE, "#4D75D8", "#FFFFFF", true);

    }

    private void checkCouponApi(BaseCustomDialog<DialogEnterCouponBinding> mEnterCouponDialog, String coupon) {
        if (mCallCheckCoupon != null) mCallCheckCoupon.cancel();
        HashMap<String, String> map = new HashMap<>();
        map.put("membership_id",String.valueOf(mPlanDataBean.getId()));
        map.put("coupon",coupon);
        mCallCheckCoupon = AppController.getInstance().getApis().checkCoupon(getHeader(), map);
        mCallCheckCoupon.enqueue(new ResponseHandler<CheckCouponBean>() {
            @Override
            public void onSuccess(CheckCouponBean checkCouponBean) {

                if (checkCouponBean != null) {
                    if (checkCouponBean.getStatus() == Constants.SUCCESS_CODE) {
                        showToast(checkCouponBean.getMessage());
                        if (checkCouponBean.getMessage().equals("Coupon Applied")) {
                            mEnterCouponDialog.dismiss();
                            binding.textCode2.setText(coupon);
                            binding.relativeCoupon2.setVisibility(View.VISIBLE);
                            discountUser(checkCouponBean.getData());
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
                    if (mEnterCouponDialog != null) {
                        mEnterCouponDialog.getBinding().editTextCode.setError("Invalid Coupon");
                    }
                }
            }
        });
    }

    private void discountUser(CouponBean checkCouponBean) {
        int currentAmount = Integer.parseInt(mPlanDataBean.getAmount());
        int totalDiscountedAmount = 0;

        switch (checkCouponBean.getType()) {
            case "1":
                Timber.d("Free");
                break;
            case "2":
                int discountAmount = (int) (currentAmount / 100.0f) * checkCouponBean.getDiscount();
                totalDiscountedAmount = currentAmount - discountAmount;
                Timber.d("Percent");
                break;
            case "3":
                totalDiscountedAmount = currentAmount - checkCouponBean.getDiscount();
                Timber.d("Fixed");
                break;
        }
//        int total = Integer.parseInt( mPlanDataBean.getAmount()) - currentAmount;

        binding.subTotal.setText(String.valueOf(currentAmount));
        binding.textBuy2.setText("Pay - \u20B9 "+String.valueOf(currentAmount));
//        binding.subTotal.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        binding.textRsOld.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (totalDiscountedAmount != 0){
            binding.couponDis.setVisibility(View.VISIBLE);
            binding.imageEditCoupon.setVisibility(View.GONE);
            int total = currentAmount - totalDiscountedAmount;
            Log.d("GHF1", String.valueOf(total));
            binding.discount.setText("-"+total);
            MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.TOTAL_DISCOUNT, String.valueOf(total));
            binding.subTotal.setText(String.valueOf(totalDiscountedAmount));
            binding.textBuy2.setText("Pay - \u20B9 "+String.valueOf(totalDiscountedAmount));
//        binding.subTotal.setTypeface(Typeface.DEFAULT_BOLD);
//        binding.textRsOne.setTypeface(Typeface.DEFAULT_BOLD);
        }

        if (totalDiscountedAmount != 0){
            int total = currentAmount - totalDiscountedAmount;
            Log.d("GHF2", String.valueOf(total));
            binding.subTotal.setText(String.valueOf(totalDiscountedAmount));
            binding.textBuy2.setText("Pay - \u20B9 "+String.valueOf(totalDiscountedAmount));
//        binding.relativeOldPrice.setVisibility(View.VISIBLE);
        }

    }

    private void getProfile() {
        showBaseProgress();
        mCallGetProfile = AppController.getInstance().getApis().getProfile(getHeader());
        mCallGetProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        PrefUtils.getInstance().setUser(model.getData());
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }
    private void getProfile2() {
        showBaseProgress();
        mCallGetProfile = AppController.getInstance().getApis().getProfile(getHeader());
        mCallGetProfile.enqueue(new ResponseHandler<LoginModel>() {
            @Override
            public void onSuccess(LoginModel model) {

                if (model != null) {
                    if (model.getStatus() == Constants.SUCCESS_CODE) {
                        PrefUtils.getInstance().setUser(model.getData());
                        MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.TOTAL_DISCOUNT, "0");
                        Intent intent = new Intent(getActivity(),SubscribeActivity.class);
                        intent.putExtra("value","1");
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<LoginModel> call_update_profile, Throwable t) {
                onCallComplete(call_update_profile, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
            }
        });
    }

}
