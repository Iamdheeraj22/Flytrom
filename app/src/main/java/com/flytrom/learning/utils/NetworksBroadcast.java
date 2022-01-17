package com.flytrom.learning.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworksBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (status != null) {
            Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show();
            //showNoNetworkDialog(status);
        } else {
            Toast.makeText(context, "internet available", Toast.LENGTH_SHORT).show();
            /*if (networkAlertDialog != null && networkAlertDialog.isShowing())
                networkAlertDialog.dismiss();*/
        }
    }
}
