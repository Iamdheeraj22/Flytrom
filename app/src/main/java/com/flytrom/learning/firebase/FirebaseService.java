package com.flytrom.learning.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.flytrom.learning.R;
import com.flytrom.learning.activities.others.HomeActivity;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

public class FirebaseService extends com.google.firebase.messaging.FirebaseMessagingService {

    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().get("message") != null) {
            EventBus.getDefault().post(remoteMessage.getData().get("message"));
            sendNotification(remoteMessage.getData().get("message"));
        }
    }

    private void sendNotification(String json) {
        try {

            if (json != null) {
                playAndSetTone();
                //PrefUtils.getInstance().setNotificationVideosIds(json);

                // The id of the channel.
                String channelId = "Default";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
                NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    CharSequence name = this.getString(R.string.title_notification);// The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(channelId, name, importance);
                    builder.setSmallIcon(R.mipmap.ic_launcher_icon_new1);
                    builder.setChannelId(channelId);
                    builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
                    if (manager != null) {
                        manager.createNotificationChannel(mChannel);
                    }
                } else {
                    builder.setSmallIcon(R.mipmap.ic_launcher_icon_new1);
                    builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
                }
                if (manager != null) {
                    manager.notify(NOTIFICATION_ID, builder.build());
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    if (manager != null) {
                        manager.createNotificationChannel(channel);
                    }
                }
                if (manager != null) {
                    manager.notify(NOTIFICATION_ID, builder.build());
                }
                wakeUpScreen();
                Intent intent = new Intent(this, HomeActivity.class);


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    private void wakeUpScreen() {

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            boolean isScreenOn = pm.isInteractive(); // check if screen is on
            if (!isScreenOn) {
                PowerManager.WakeLock wl = pm.newWakeLock(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
                wl.acquire(3000); //set your time in milliseconds
            }
        }
    }

    private void playAndSetTone() {
        MediaPlayer mMediaPlayer = MediaPlayer.create(this, R.raw.tone_notification);
        mMediaPlayer.start();
    }
}
