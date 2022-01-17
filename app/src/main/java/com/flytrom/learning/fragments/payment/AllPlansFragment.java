package com.flytrom.learning.fragments.payment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.ProgressActivity;
import com.flytrom.learning.activities.info.ProfileActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.adapters.AllPlansAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.payment_beans.GetPlansBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanDataBean;
import com.flytrom.learning.beans.response_beans.payment_beans.PlanMetadataBean;
import com.flytrom.learning.databinding.FragmentAllPlansBinding;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.BackStackManager;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.ConstantsNew;
import com.flytrom.learning.utils.MySharedPreferences;
import com.flytrom.learning.utils.ResponseHandler;
import com.flytrom.learning.utils.VerticalPagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import timber.log.Timber;

public class AllPlansFragment extends BaseFragment<FragmentAllPlansBinding>
        implements VerticalPagination.VerticalScrollListener, BaseCallback,
        SimpleRecyclerViewAdapter.SimpleCallback<PlanMetadataBean> {

    private AllPlansAdapter mAllPlansAdapter;
    private Call<GetPlansBean> mCallGetPlans;
    private int mCurrentPage = 1;
    String go = "";

    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        go = MySharedPreferences.getInstance().getString(getActivity(), ConstantsNew.OWNER_ID);
        if (go.equalsIgnoreCase("1")){
            goToMyPurchasedPlan();
            MySharedPreferences.getInstance().saveString(getActivity(), ConstantsNew.OWNER_ID,"0");
        }
        initView();
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_all_plans;
    }

    private void initView() {

        mAllPlansAdapter = new AllPlansAdapter(getActivity(), this, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        VerticalPagination mVerticalPagination = new VerticalPagination(linearLayoutManager, 3);
        mVerticalPagination.setListener(this);
        binding.rvPlans.setLayoutManager(linearLayoutManager);
        binding.rvPlans.addOnScrollListener(mVerticalPagination);
        binding.rvPlans.setAdapter(mAllPlansAdapter);

        setBaseCallback(baseCallback);
        if (checkIsInternetOn()) getProPlans(mCurrentPage, true);
        else showEmptyView();

    }

    @Override
    public void onDetach() {
        if (mCallGetPlans != null) mCallGetPlans.cancel();
        super.onDetach();
    }

    @Override
    public void onLoadMore() {
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.image_back:
                goBack();
                break;
            case R.id.text_my_subs:
                goToMyPurchasedPlan();
                break;
        }
    };

    private void getProPlans(int currentPage, boolean progress) {
        showBaseProgress();
        mCallGetPlans = AppController.getInstance().getApis().getPlans(getHeader(),
                String.valueOf(currentPage));
        mCallGetPlans.enqueue(new ResponseHandler<GetPlansBean>() {
            @Override
            public void onSuccess(GetPlansBean getPlansBean) {

                if (getPlansBean != null) {
                    if (getPlansBean.getStatus() == Constants.SUCCESS_CODE) {

                        if (getPlansBean.getData() != null && getPlansBean.getData().size() > 0) {

                            int size = mAllPlansAdapter.getData().size();
                            for (int i = size; i < getPlansBean.getData().size(); i++) {
                                getPlansBean.getData().get(i).setIndex(i);
                            }

                            if (mCurrentPage == 1)
                                mAllPlansAdapter.setDataList(getPlansBean.getData());
                            else
                                mAllPlansAdapter.addToDataList(getPlansBean.getData());
                        }
                    }
                }
            }

            @Override
            public void apiError(ApiErrorBean t) {
                hideBaseProgress();
                if (t != null) showErrorToast(t.getMessage());
            }

            @Override
            public void onComplete(Call<GetPlansBean> call, Throwable t) {
                onCallComplete(call, t);
            }

            @Override
            public void statusCode(int t) {
                super.statusCode(t);
                handleCodes(t);
                if (t == Constants.NO_DATA) {
                    if (mCurrentPage == 1) {
                        showEmptyView();
                    }
                }
            }
        });
    }

    private void showEmptyView() {
        binding.rvPlans.setVisibility(View.GONE);
        binding.linearEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        Timber.d("dfdf");
    }

    @Override
    public void onViewClick(View view, int position) {

        PlanDataBean planDataBean = mAllPlansAdapter.getData().get(position);
        if (planDataBean.getHasMeta().equals("1")) {
            if (planDataBean.isSelected()) planDataBean.setSelected(false);
            else planDataBean.setSelected(true);
            mAllPlansAdapter.notifyItemChanged(position);
        } else {
            goToPurchasePlan(mAllPlansAdapter.getData().get(position));
        }
    }

    private void goToPurchasePlan(PlanDataBean planDataBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", planDataBean);
        PurchasePlanFragment fragment = new PurchasePlanFragment();
        fragment.setArguments(bundle);
        BackStackManager.getInstance(getActivity()).pushSubFragments(R.id.container_subscribe, SubscribeActivity.TAG, fragment, true);
    }

    private void goToMyPurchasedPlan() {
        SubscribeActivity.mInstance.setToolBarTitle(getString(R.string.text_my_subscription_small));
        BackStackManager.getInstance(getActivity()).pushSubFragments(R.id.container_subscribe,
                SubscribeActivity.TAG, new MyPurchasedPlanFragment(), true);
    }

    @Override
    public void onClick(View v, PlanMetadataBean dataBean) {
        if (v.getId()==R.id.buy6mon){
            List<PlanMetadataBean> planMetadata=new ArrayList<>();
            planMetadata.add(dataBean);
            PlanDataBean planDataBean = new PlanDataBean();
            planDataBean.setId(Integer.parseInt(dataBean.getPlanId()));
            planDataBean.setPlan_metadata_id(Integer.parseInt(dataBean.getId()));
            planDataBean.setAmount(dataBean.getAmount2());
            planDataBean.setPlanName(dataBean.getTitle());
            planDataBean.setPlanType(dataBean.getType());
            planDataBean.setSubjects(dataBean.getSubjects());
            planDataBean.setHasMeta("1");
            planDataBean.setOldAmount(dataBean.getOldAmount2());
            planDataBean.setValidity(dataBean.getValidity2());
            planDataBean.setPlanMetadata(planMetadata);
            goToPurchasePlan(planDataBean);
        }else if (v.getId()==R.id.buythreeMon){
            List<PlanMetadataBean> planMetadata=new ArrayList<>();
            planMetadata.add(dataBean);
            PlanDataBean planDataBean = new PlanDataBean();
            planDataBean.setId(Integer.parseInt(dataBean.getPlanId()));
            planDataBean.setPlan_metadata_id(Integer.parseInt(dataBean.getId()));
            planDataBean.setAmount(dataBean.getAmount());
            planDataBean.setPlanName(dataBean.getTitle());
            planDataBean.setPlanType(dataBean.getType());
            planDataBean.setSubjects(dataBean.getSubjects());
            planDataBean.setHasMeta("1");
            planDataBean.setValidity(dataBean.getValidity());
            planDataBean.setOldAmount(dataBean.getOldAmount());
            planDataBean.setPlanMetadata(planMetadata);
            goToPurchasePlan(planDataBean);
        }

    }
}
