package com.flytrom.learning.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.LoginActivity;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.beans.normal_beans.ApiErrorBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.databinding.DialogPurchasePlanBinding;
import com.flytrom.learning.room_db.repository.FlytromDBRepository;
import com.flytrom.learning.room_db.repository.FlytromDBRepositoryKotlin;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.MyToast;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ProgressDialogAvl;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Annotation;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import timber.log.Timber;

public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment {

    protected Context basecontext;
    protected ProgressDialogAvl progressDialogAvl;
    protected Vibrator vibrator;
    protected V binding;
    protected boolean isInternetOn;
    private BaseCustomDialog<DialogPurchasePlanBinding> mDialogPurchasePlan;
    //this mDBRepositoryKotlin is for insert,update and delete in db
    protected FlytromDBRepositoryKotlin mDBRepositoryKotlin;

    //this mDBRepository is for only getting data from db
    protected FlytromDBRepository mDBRepository;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.basecontext = context;
        this.vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public void onDetach() {
        if (progressDialogAvl != null && progressDialogAvl.isShowing())
            progressDialogAvl.dismiss();

        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetOnOff(String status) {
        if (status != null) {
            isInternetOn = status.equals("1");
        }

        Timber.d("dfd");
    }

    protected void setBaseCallback(BaseCallback baseCallback) {
        binding.setVariable(com.flytrom.learning.BR.callback, baseCallback);
    }

    protected void showPurchasePlanDialog(String message) {
        mDialogPurchasePlan = new BaseCustomDialog<>(Objects.requireNonNull(getActivity()), R.layout.dialog_purchase_plan, view -> {
            switch (view.getId()) {
                case R.id.btn_view_plans:
                    mDialogPurchasePlan.dismiss();
                    goAllPlanScreen();
                    break;
                case R.id.text_dismiss:
                    mDialogPurchasePlan.dismiss();
                    break;
            }
        });
        mDialogPurchasePlan.getBinding().textMessage.setText(String.format("The %s is available only for pro users. Want to check Pro Plans?", message));
        mDialogPurchasePlan.setCancelable(true);
        mDialogPurchasePlan.show();
    }

    private void goAllPlanScreen() {
        basecontext.startActivity(new Intent(basecontext, SubscribeActivity.class));
        goNextAnimation();
    }


    protected Map<String, String> getHeader() {
        Map<String, String> map = new HashMap<>();
        UserDataBean bean = PrefUtils.getInstance().getUser();
        if (bean != null) {
            map.put(Constants.KEY_USER_ID, bean.getId());
            map.put(Constants.KEY_SESSION_ID, bean.getSession_id());
        }
        return map;
    }


    protected <T> void onCallComplete(Call<T> call, Throwable t) {
        hideBaseProgress();
        if (!call.isCanceled() && t != null) {
            showToast(resolveNetworkError(t));
        }
    }

    protected boolean checkIsInternetOn() {
        if (AppController.getInstance().isInternetOn()) return true;
        else {
            showToast(getString(R.string.no_internet));
            return false;
        }
    }

    protected void goBack() {
        Objects.requireNonNull(getActivity()).onBackPressed();
        getActivity().overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected void goNextAnimation() {
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    private String resolveNetworkError(Throwable cause) {
        if (cause instanceof UnknownHostException)
            return basecontext.getString(R.string.no_internet);
        else if (cause instanceof SocketTimeoutException)
            return basecontext.getString(R.string.server_error);
        else if (cause instanceof ConnectException)
            return basecontext.getString(R.string.no_internet);
        else if (cause instanceof JsonSyntaxException)
            return basecontext.getString(R.string.parser_error);
        return basecontext.getString(R.string.wrong);
    }

    public void addFragment(Context context, Fragment fragment, int container) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(container, fragment)
                .commitAllowingStateLoss();
    }

    protected void showSnack(View parent, String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.text_ok), v -> {

                })
                .show();
    }

    public void replaceFragment(Context context, Fragment fragment, int container) {
        ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(container, fragment)
                .commitAllowingStateLoss();
    }

    /* public void replaceFragment(Context context, Fragment fragment, int container, boolean animate) {
         FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
         transaction.replace(container, fragment);
         transaction.commitAllowingStateLoss();
     }
     */


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getFragmentLayout(), container, false);
        progressDialogAvl = new ProgressDialogAvl(basecontext);
        mDBRepositoryKotlin = new FlytromDBRepositoryKotlin(Objects.requireNonNull(getActivity()).getApplication());
        mDBRepository = new FlytromDBRepository(Objects.requireNonNull(getActivity()).getApplication());
        progressDialogAvl.setCancelable(false);
        onFragmentCreateView(savedInstanceState);
        return binding.getRoot();
    }

    protected void showSuccessToast(String message) {
        MyToast.success(basecontext, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showInfoToast(String message) {
        MyToast.info(basecontext, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showWarnToast(String message) {
        MyToast.warning(basecontext, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showErrorToast(Throwable t) {
        try {
            showErrorToast(t.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void showErrorToast(Exception t) {
        try {
            showErrorToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void showErrorToast(String t) {
        MyToast.error(basecontext, "" + t, Toast.LENGTH_LONG, true).show();
    }

    protected void showToast(String message) {
        Toast.makeText(basecontext, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(Throwable t) {
        try {
            showToast(t.getMessage());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void showToast(Exception t) {
        try {
            showToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @CallSuper
    protected void onFragmentCreateView(Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public abstract int getFragmentLayout();

    public void showBaseProgress() {
        progressDialogAvl.show();
    }

    public void hideBaseProgress() {
        if (progressDialogAvl != null && progressDialogAvl.isShowing())
            progressDialogAvl.dismiss();
    }

    protected <T> void handleError(Response<T> response) {
        Converter<ResponseBody, ApiErrorBean> converter = AppController.getInstance().getRetrofit().responseBodyConverter(ApiErrorBean.class, new Annotation[0]);
        ApiErrorBean error = null;
        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (error != null) {
            DebugLog("error Message : " + error.getMessage());
            DebugLog("error Status: " + error.getStatus());
            DebugLog("error Method: " + error.getMethod());
            showToast("" + error.getMessage());
            handleCodes(error.getStatus());

        }
    }

    protected void handleCodes(int s) {
        if (s == 401) {
            logoutUser();
        }
    }

    private void DebugLog(String message) {
        if (Constants.SHOW_LOG)
            Log.d(getActivity().getClass().getSimpleName(), "" + message);
    }

    private void logoutUser() {
        if (PrefUtils.getInstance().getUser() != null) {
            if (PrefUtils.getInstance().clear()) {
                Intent intent = new Intent(basecontext, LoginNewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                ActivityCompat.finishAffinity(getActivity());
            }
        }
    }

}
