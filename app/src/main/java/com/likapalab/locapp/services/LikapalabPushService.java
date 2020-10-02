/*
 * Created by Mehmet Ozdemir on 8/31/20 10:06 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 10:00 AM
 */

package com.likapalab.locapp.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.likapalab.locapp.activities.LoginActivity;
import com.likapalab.locapp.helpers.VenueHelper;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.activities.MainActivity;
import com.likapalab.locapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LikapalabPushService extends HmsMessageService {

    //Class Variables
    private final String TAG = LikapalabPushService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (!remoteMessage.getData().isEmpty()) {
            try {
                JSONObject remoteDataObject = new JSONObject(remoteMessage.getData());
                if (remoteDataObject.has(Constants.PUSH_DATA_PARAMETER_ACTION)) {
                    String action = remoteDataObject.getString(Constants.PUSH_DATA_PARAMETER_ACTION);
                    Log.d(TAG, "Notification action is " + action);
                    if (action.equals(Constants.PUSH_DATA_VALUE_USER_NOTIFY)) {
                        String title = remoteDataObject.getString(Constants.PUSH_DATA_PARAMETER_TITLE);
                        String body = remoteDataObject.getString(Constants.PUSH_DATA_PARAMETER_BODY);
                        createNotification(title, body);
                    } else if (action.equals(Constants.PUSH_DATA_VALUE_CHECK_VENUE_LIST)) {
                        VenueHelper.checkVenueList(this);
                    } else {
                        Log.d(TAG, "Undefined action received.");
                    }
                } else {
                    Log.d(TAG, "No action parameter in remote message's data.");
                }
            } catch (Throwable t) {
                Log.e(TAG, "Could not parse malformed JSON: " + remoteMessage.getData());
            }
        } else {
            Log.d(TAG, "Remote message's data not found.");
        }
    }

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, s);
        super.onNewToken(s);
        broadcastPushToken(s);
    }

    private void broadcastPushToken(String pushToken) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_PUSH_TOKEN_RECEIVED);
        intent.putExtra(Constants.INTENT_PARAMETER_PUSH_TOKEN, pushToken);
        sendBroadcast(intent);
    }

    private void createNotification(String title, String body) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels();
        }

        Log.d(TAG, "Notification is being created.");
        Intent intentActivity = new Intent(this, LoginActivity.class);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(this, 0, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID_USER_NOTIFY)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntentActivity)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(Constants.NOTIFICATION_ID_USER_NOTIFY, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels() {
        Log.d(TAG, "Notification channels are being created.");
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        List<NotificationChannel> channels = new ArrayList<>();
        if (notificationmanager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_USER_NOTIFY) == null) {
            CharSequence name = Constants.NOTIFICATION_CHANNEL_NAME_USER_NOTIFY;
            channels.add(new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_USER_NOTIFY, name, NotificationManager.IMPORTANCE_HIGH));
        }
        notificationmanager.createNotificationChannels(channels);
    }
}
