package com.flytrom.learning.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.flytrom.learning.utils.AppController;

public class ConnectionStateMonitor extends ConnectivityManager.NetworkCallback {

    final NetworkRequest networkRequest;

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        AppController.getInstance().setInternetOn(true);
        //EventBus.getDefault().post("1");
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        AppController.getInstance().setInternetOn(false);
        //EventBus.getDefault().post("0");
    }

    public ConnectionStateMonitor() {
        networkRequest = new NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build();
    }

    public void enable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(networkRequest, this);
        }
    }
}
