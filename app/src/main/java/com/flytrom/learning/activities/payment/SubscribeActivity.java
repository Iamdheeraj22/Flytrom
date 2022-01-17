package com.flytrom.learning.activities.payment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.base.BaseActivity;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.databinding.ActivitySubscribeBinding;
import com.flytrom.learning.databinding.PartialToolbarBackBinding;
import com.flytrom.learning.databinding.PartialToolbarBinding;
import com.flytrom.learning.fragments.payment.AllPlansFragment;
import com.flytrom.learning.fragments.payment.MyPurchasedPlanFragment;
import com.flytrom.learning.fragments.payment.PurchasePlanFragment;
import com.flytrom.learning.interfaces.IOnBackpresed;
import com.flytrom.learning.model.LoginModel.LoginModel;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.BackStackManager;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ResponseHandler;

import java.util.HashMap;
import java.util.Objects;
import java.util.Stack;

import retrofit2.Call;

public class SubscribeActivity extends BaseActivity<ActivitySubscribeBinding> {

    public static final String TAG = "SubscribeActivity";
    private LinearLayout mLinearLayoutParent;
    private Call<LoginModel> mCallGetProfile;
    public static SubscribeActivity mInstance;
    public PartialToolbarBackBinding toolbar;



    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        initView();
        getProfile();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_subscribe;
    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    protected void onDestroy() {
        BackStackManager.getInstance(this).removeKey(TAG);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //toolbar.imageBack.performClick();

        int count = getSupportFragmentManager().getBackStackEntryCount();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container_subscribe);

        if (fragment instanceof MyPurchasedPlanFragment) {

            if (!(fragment instanceof IOnBackpresed) || !((IOnBackpresed) fragment).onBackPressed()) {

            } else {
                toolbar.imageBack.performClick();
            }
        }else {

            if (count <= 1) {

                toolbar.imageBack.performClick();

            } else {
                getSupportFragmentManager().popBackStack();
            }

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            HashMap<String, Stack<Fragment>> map = BackStackManager.getInstance(this).getBackStack();
            Stack<Fragment> fragments = map.get(BackStackManager.getInstance(this).getCurrentTab());

            if (fragments != null) {
                PurchasePlanFragment purchasePlanFragment = (PurchasePlanFragment) fragments.lastElement();
                purchasePlanFragment.activityResult(data);
            }
        }
    }

    public LinearLayout getLinearLayoutParent() {
        return mLinearLayoutParent;
    }

    private void initView() {
        mLinearLayoutParent = binding.linearParent;
        setBaseCallback(baseCallback);
        toolbar = binding.toolbar;
        mInstance = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("from") != null){
                if (bundle.getString("from").equals("my_subs")) {
                    binding.toolbar.textTitle.setText(R.string.title_my_plans);
                    binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                    BackStackManager.getInstance(this).removeKey(TAG);
                    BackStackManager.getInstance(this).pushFragments(R.id.container_subscribe, TAG,
                            new MyPurchasedPlanFragment(), true);
                }
            }
        } else {

            BackStackManager.getInstance(this).removeKey(TAG);
            BackStackManager.getInstance(this).pushFragments(R.id.container_subscribe, TAG, new AllPlansFragment(), true);
            setToolBarTitle("Pro Plans");
        }

        Intent intent = getIntent();
        String value = "0";
        value = intent.getStringExtra("value");
        if (value != null){
            if (value.equalsIgnoreCase("1")){
                binding.toolbar.textTitle.setText(R.string.title_my_plans);
                binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
                BackStackManager.getInstance(this).removeKey(TAG);
                BackStackManager.getInstance(this).pushFragments(R.id.container_subscribe, TAG,
                        new MyPurchasedPlanFragment(), true);
                value = "0";
            }

        }
    }

    public void setToolBarTitle(String title) {
        binding.toolbar.textTitle.setText(title);
        binding.toolbar.textTitle.setTextColor(Color.parseColor("#484848"));
    }
    public void setToolBarHide(String title) {
        if (title.equalsIgnoreCase("0")){
          binding.toolbar.toolRL.setVisibility(View.GONE);
        }else {
          binding.toolbar.toolRL.setVisibility(View.VISIBLE);
        }

    }

    private BaseCallback baseCallback = view -> {
        if (view.getId() == R.id.image_back) {
            Stack<Fragment> authFragments = BackStackManager.getInstance(this).getBackStack().get(TAG);
            if (authFragments != null) {
                if (authFragments.size() == 1) {
                    finishActivityWithBackAnim();
                } else {
                    setToolBarTitle("Pro Plans");
                    BackStackManager.getInstance(this).removeSubFragment(TAG, R.id.container_subscribe, true);
                }

            } else {
                finishActivityWithBackAnim();
            }
        }
    };

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
}
