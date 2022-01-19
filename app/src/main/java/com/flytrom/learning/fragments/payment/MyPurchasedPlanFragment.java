package com.flytrom.learning.fragments.payment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.arvind.android.permissions.PermissionHandler;
import com.arvind.android.permissions.Permissions;
import com.flytrom.learning.DownloadFileTask;
import com.flytrom.learning.Downloader;
import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.ChangePasswordActivity;
import com.flytrom.learning.activities.auth.ValidatePhoneActivity;
import com.flytrom.learning.activities.changeNumEmail.ChangePhoneOrrEmailActivity;
import com.flytrom.learning.activities.others.EditProfileActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.adapters.MyPurchasedPlansAdapter;
import com.flytrom.learning.base.BaseCallback;
import com.flytrom.learning.base.BaseFragment;
import com.flytrom.learning.base.SimpleRecyclerViewAdapter;
import com.flytrom.learning.beans.response_beans.auth.PurchasedPlansBean;
import com.flytrom.learning.databinding.BottomSheetViewReceiptBinding;
import com.flytrom.learning.databinding.FragmentMyPurchasedPlanBinding;
import com.flytrom.learning.interfaces.IOnBackpresed;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.UtilMethods;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import timber.log.Timber;

public class MyPurchasedPlanFragment extends BaseFragment<FragmentMyPurchasedPlanBinding> implements IOnBackpresed {

    String pdfUrl;
    String expDate;
    @Override
    protected void onFragmentCreateView(Bundle savedInstanceState) {
        super.onFragmentCreateView(savedInstanceState);
        initView();
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_my_purchased_plan;
    }

    private void initView() {
        setBaseCallback(baseCallback);
        MyPurchasedPlansAdapter myPurchasedPlansAdapter = new MyPurchasedPlansAdapter(getActivity(), new SimpleRecyclerViewAdapter.SimpleCallback<PurchasedPlansBean>() {
            @Override
            public void onClick(View v, PurchasedPlansBean purchasedPlansBean) {

            }

            @Override
            public void onClickWithPosition(View v, PurchasedPlansBean purchasedPlansBean, int pos) {
                if (v.getId() == R.id.view_receipt) {
                    if (purchasedPlansBean != null) {
                        pdfUrl = purchasedPlansBean.getInvoice_pdf();
                        new RetrivePDFfromUrl().execute("https://learningapp.flytrom.com/media/"+pdfUrl);
                        SubscribeActivity.mInstance.setToolBarHide("0");
                        binding.elPdf.setVisibility(View.VISIBLE);
//                        showBottomSheetDialogReceipt(purchasedPlansBean);
                    }
                }
            }
        });


        binding.rvPlans.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvPlans.setAdapter(myPurchasedPlansAdapter);
        List<PurchasedPlansBean> plans = PrefUtils.getInstance().getUser().getPurchasedPlans();
        if (plans != null && plans.size() > 0) {
            myPurchasedPlansAdapter.setDataList(plans);
        } else {
            binding.linearEmptyView.setVisibility(View.VISIBLE);
        }


    }


    private void showBottomSheetDialogReceipt(PurchasedPlansBean planBean) {

        int currentAmount;
        LayoutInflater inflater = (LayoutInflater) basecontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        BottomSheetViewReceiptBinding sheetBinding = BottomSheetViewReceiptBinding.inflate(inflater);

        BottomSheetDialog sheetDialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()), R.style.CustomBottomSheetDialogTheme);
        sheetDialog.setContentView(sheetBinding.getRoot());

        sheetBinding.textPlanName.setText(planBean.getPlanDetails().getPlanName());

        if (planBean.getPlanDetails().getHasMeta().equals("1")) {
            sheetBinding.textAmount.setText(planBean.getPlanMetadataBean().getAmount());
            currentAmount = Integer.parseInt(planBean.getPlanMetadataBean().getAmount());
        } else {
            sheetBinding.textAmount.setText(planBean.getPlanDetails().getAmount());
            currentAmount = Integer.parseInt(planBean.getPlanDetails().getAmount());
        }
        sheetBinding.textPaymentMode.setText(String.format("Payment Mode : %s", planBean.getPaymentMode()));
        sheetBinding.textStatus.setText(String.format("Status : %s", planBean.getTxStatus()));
        sheetBinding.textDate.setText(String.format("Date : %s", UtilMethods.splitString(planBean.getTxTime())));
        sheetBinding.textReferenceId.setText(String.format("Reference Id : %s", planBean.getReferenceId()));
        sheetBinding.btnClose.setOnClickListener(v -> sheetDialog.dismiss());

        if (planBean.getUserCouponApplied() != null) {
            sheetBinding.relativeCouponCode.setVisibility(View.VISIBLE);
            int totalDiscountedAmount = 0;
            switch (planBean.getUserCouponApplied().getCouponType()) {
                case "1":
                    Timber.d("Free");
                    sheetBinding.textCc.setText(R.string._0_percent_discount);
                    break;
                case "2":
                    int discountAmount = (int) (currentAmount / 100.0f) * planBean.getUserCouponApplied().getDiscount();
                    totalDiscountedAmount = currentAmount - discountAmount;
                    sheetBinding.textCc.setText(String.format("%s%%", planBean.getUserCouponApplied().getDiscount()));
                    Timber.d("Percent");
                    break;
                case "3":
                    totalDiscountedAmount = currentAmount - planBean.getUserCouponApplied().getDiscount();
                    sheetBinding.textCc.setText(String.format("Rs.%s", planBean.getUserCouponApplied().getDiscount()));
                    Timber.d("Fixed");
                    break;
            }

            sheetBinding.textOldAmount.setText(String.valueOf(currentAmount));
            sheetBinding.textOldAmount.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            sheetBinding.textRsOld.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if (totalDiscountedAmount != 0)
                sheetBinding.textAmount.setText(String.valueOf(totalDiscountedAmount));
            sheetBinding.textAmount.setTypeface(Typeface.DEFAULT_BOLD);
            sheetBinding.textRsOne.setTypeface(Typeface.DEFAULT_BOLD);
            sheetBinding.relativeOldPrice.setVisibility(View.VISIBLE);

        } else {
            sheetBinding.relativeCouponCode.setVisibility(View.GONE);
            sheetBinding.relativeOldPrice.setVisibility(View.GONE);
        }

        sheetDialog.show();
    }

    private BaseCallback baseCallback = view -> {
        switch (view.getId()) {
            case R.id.cnclPdf:
                SubscribeActivity.mInstance.setToolBarHide("1");
                binding.elPdf.setVisibility(View.GONE);
                break;
          case R.id.downloadPdf:
              new DownloadFileTask(getActivity()).execute("https://learningapp.flytrom.com/media/"+pdfUrl);
        }
    };

    @Override
    public Boolean onBackPressed() {
        if (binding.elPdf.getVisibility() == View.VISIBLE){
            binding.elPdf.setVisibility(View.GONE);
            return false;
        }else{
            return true;
        }
    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            binding.idPDFView.fromStream(inputStream).load();
        }
    }
}
