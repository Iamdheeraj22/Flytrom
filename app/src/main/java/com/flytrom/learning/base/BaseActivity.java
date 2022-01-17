package com.flytrom.learning.base;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.auth.LoginActivity;
import com.flytrom.learning.activities.loginNew.LoginNewActivity;
import com.flytrom.learning.activities.others.HomeActivity;
import com.flytrom.learning.activities.payment.SubscribeActivity;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.databinding.DialogPurchasePlanBinding;
import com.flytrom.learning.room_db.repository.FlytromDBRepository;
import com.flytrom.learning.room_db.repository.FlytromDBRepositoryKotlin;
import com.flytrom.learning.utils.AppController;
import com.flytrom.learning.utils.Constants;
import com.flytrom.learning.utils.NetworkUtil;
import com.flytrom.learning.utils.NetworksBroadcast;
import com.flytrom.learning.utils.PrefUtils;
import com.flytrom.learning.utils.ProgressDialogAvl;
import com.flytrom.learning.utils.RuntimePermissionsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import timber.log.Timber;


/**
 * Created by macminiandroid on 31/07/18.
 */

public abstract class BaseActivity<V extends ViewDataBinding> extends RuntimePermissionsActivity {

    Context context;
    protected ProgressDialogAvl progressDialogAvl;
    public boolean check_session = true;
    private static final String TAG = BaseActivity.class.getName();
    protected V binding;
    protected Vibrator vibrator;
    protected boolean isInternetOn;
    private BaseCustomDialog<DialogPurchasePlanBinding> mDialogPurchasePlan;

    //this mDBRepositoryKotlin is for insert,update and delete in db
    protected FlytromDBRepositoryKotlin mDBRepositoryKotlin;

    //this mDBRepository is for only getting data from db
    protected FlytromDBRepository mDBRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mDBRepositoryKotlin = new FlytromDBRepositoryKotlin(getApplication());
        mDBRepository = new FlytromDBRepository(getApplication());
        binding = DataBindingUtil.setContentView(this, getContentView());
        this.context = this;
        progressDialogAvl = new ProgressDialogAvl(this);
        progressDialogAvl.setCancelable(false);
        onViewReady(savedInstanceState, getIntent());
        vibrator = (Vibrator) Objects.requireNonNull(this).getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotification(String message) {
        if (message != null) {
            Timber.d("df");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetOnOff(String status) {
        if (status != null) {
            if (status.equals("1"))
                isInternetOn = true;
            else
                isInternetOn = false;
        }

        Timber.d("dfd");
    }

    protected String getBaseDirectoryPath() {
        return Objects.requireNonNull(context.getExternalFilesDir(null)).getAbsolutePath() + "/Flytrom/";
    }

    public void showToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
    }

    public void showBaseProgress() {
        progressDialogAvl.show();
    }

    protected void goNextAnimation() {
        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
    }

    protected void goBackAnimation() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    protected void goBack() {
        onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }


    protected void finishActivityWithBackAnim() {
        finish();
        goBackAnimation();
    }

    protected void gotoWhatsUp() {
        PackageManager packageManager = context.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(Constants.WHATS_APP_URL));
            if (i.resolveActivity(packageManager) != null) {
                context.startActivity(i);
            } else showToast("WhatsApp not installed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        if (progressDialogAvl != null && progressDialogAvl.isShowing())
            progressDialogAvl.dismiss();
        super.onDestroy();
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
    }

    public void hideBaseProgress() {
        if (progressDialogAvl != null && progressDialogAvl.isShowing())
            progressDialogAvl.dismiss();
    }

    public void startTimer() {

    }

    protected <T> void onCallComplete(Call<T> call, Throwable t) {
        hideBaseProgress();
        if (!call.isCanceled() && t != null) {
            showErrorToast(resolveNetworkError(t));
        }
    }

    protected String resolveNetworkError(Throwable cause) {
        if (cause instanceof UnknownHostException)
            return context.getString(R.string.no_internet);
        else if (cause instanceof SocketTimeoutException)
            return context.getString(R.string.server_error);
        else if (cause instanceof ConnectException)
            return context.getString(R.string.no_internet);
        else if (cause instanceof JsonSyntaxException)
            return context.getString(R.string.parser_error);
        return context.getString(R.string.wrong);
    }

    protected boolean checkIsInternetOn() {
        if (AppController.getInstance().isInternetOn()) return true;
        else {
            showToast(getString(R.string.no_internet));
            return false;
        }
    }

    protected void handleCodes(int s) {
        if (s == 401) {
            logoutUser();
        }
    }

    protected void logoutUser() {
        if (PrefUtils.getInstance().getUser() != null) {
            if (PrefUtils.getInstance().clear()) {
                Intent intent = new Intent(context, LoginNewActivity   .class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                goNextAnimation();
                ActivityCompat.finishAffinity(BaseActivity.this);
            }
        }
    }

    protected void showSnack(View parent, String message) {
        Snackbar.make(parent, message, Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.text_ok), v -> {

                })
                .show();
    }

    protected void goToHomeScreen() {
        if (PrefUtils.getInstance().getUser() != null) {

            UserDataBean loginBean = PrefUtils.getInstance().getUser();

            Intent intent;

            if (loginBean != null) {
                intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        }
    }

    protected void showPurchasePlanDialog(String message) {
        mDialogPurchasePlan = new BaseCustomDialog<>(context, R.layout.dialog_purchase_plan, view -> {
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
        mDialogPurchasePlan.getBinding().textMessage.setText(String.format("The %s is available only after purchasing Paid Plan. Want to check Pro Plans?", message));
        mDialogPurchasePlan.setCancelable(true);
        mDialogPurchasePlan.show();
    }

    private void goAllPlanScreen() {
        startActivity(new Intent(context, SubscribeActivity.class));
        goNextAnimation();
    }

    public void DebugLog(String message) {
        if (Constants.SHOW_LOG)
            Log.d(context.getClass().getSimpleName(), "" + message);
    }

    @Override
    protected void onPause() {
        super.onPause();
        check_session = false;
    }

    protected abstract int getContentView();

    protected void setBaseCallback(BaseCallback baseCallback) {
        binding.setVariable(com.flytrom.learning.BR.callback, baseCallback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        //Log.e("TAG", "Activity Minimized");
    }

    protected void showSuccessToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        //MyToast.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showInfoToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        //MyToast.info(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showWarnToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        //MyToast.warning(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void showErrorToast(Throwable t) {
        try {
            showErrorToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Map<String, String> getHeader() {
        Map<String, String> map = new HashMap<>();
        UserDataBean bean = PrefUtils.getInstance().getUser();
        if (bean != null) {
            map.put(Constants.KEY_SESSION_ID,bean.getSession_id());
            map.put(Constants.KEY_USER_ID, bean.getId());
            Log.d("Test"," == "+bean.getSession_id()+" : "+bean.getId());
        }
        return map;
    }

    protected void showErrorToast(Exception t) {
        try {
            showErrorToast(t.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void showErrorToast(String message) {
        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
        //MyToast.error(context, "" + t, Toast.LENGTH_LONG, true).show();
    }

    protected void hideKeyboard(){
        try {
            InputMethodManager imm2 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm2.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

