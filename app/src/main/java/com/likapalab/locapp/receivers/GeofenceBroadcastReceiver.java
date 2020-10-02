/*
 * Created by Mehmet Ozdemir on 9/3/20 5:34 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/3/20 5:34 PM
 */

package com.likapalab.locapp.receivers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.huawei.hms.location.Geofence;
import com.huawei.hms.location.GeofenceData;
import com.likapalab.locapp.R;
import com.likapalab.locapp.activities.LoginActivity;
import com.likapalab.locapp.helpers.VenueHelper;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    //Class Constants
    private final String TAG = GeofenceBroadcastReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Geofence action received.");
        GeofenceData geofenceData = GeofenceData.getDataFromIntent(intent);
        if (geofenceData != null) {
            int conversion = geofenceData.getConversion();
            ArrayList<Geofence> geofenceTransition = (ArrayList<Geofence>) geofenceData.getConvertingGeofenceList();
            Geofence geofence = geofenceTransition.get(0);

            ArrayList<Venue> venueList = VenueHelper.getVenueList(context);
            Venue venue = null;
            for (int i = 0; i < venueList.size(); i++) {
                if (geofence.getUniqueId().equals(String.valueOf(venueList.get(i).getId()))) {
                    venue = venueList.get(i);
                    break;
                }
            }
            if (venue == null) {
                Log.d(TAG, "Received geofence's venue id is could not found in recorded venue list.");
                return;
            }

            String notificationMessage = "";
            switch (conversion) {
                case Geofence.ENTER_GEOFENCE_CONVERSION:
                    notificationMessage = context.getString(R.string.text_welcome_message, venue.getName());
                    prepareAndPostEventToAnalytics(Constants.ANALYTICS_EVENT_NAME_GEOFENCE_ENTER, venue);
                    break;
                case Geofence.EXIT_GEOFENCE_CONVERSION:
                    notificationMessage = context.getString(R.string.text_good_bye_message);
                    prepareAndPostEventToAnalytics(Constants.ANALYTICS_EVENT_NAME_GEOFENCE_EXIT, venue);
                    break;
                default:
                    Log.d(TAG, "Unknown conversion type. Notification could not created.");
                    return;
            }
            createNotification(context, notificationMessage);
        } else {
            Log.d(TAG, "Geofence data not found.");
        }
    }

    private void createNotification(Context context, String notificationMessage) {
        Log.d(TAG, "Notification message: " + notificationMessage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannels(context);
        }

        Log.d(TAG, "Notification is being created.");
        Intent intentActivity = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntentActivity = PendingIntent.getActivity(context, 0, intentActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID_GEOFENCE_NOTIFY)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setContentIntent(pendingIntentActivity)
                .setAutoCancel(true);

        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationmanager.notify(Constants.NOTIFICATION_ID_GEOFENCE_NOTIFY, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannels(Context context) {
        Log.d(TAG, "Notification channels are being created.");
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        List<NotificationChannel> channels = new ArrayList<>();
        if (notificationmanager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_GEOFENCE_NOTIFY) == null) {
            CharSequence name = Constants.NOTIFICATION_CHANNEL_NAME_GEOFENCE_NOTIFY;
            channels.add(new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID_GEOFENCE_NOTIFY, name, NotificationManager.IMPORTANCE_HIGH));
        }
        notificationmanager.createNotificationChannels(channels);
    }

    private void prepareAndPostEventToAnalytics(String eventName, Venue venue) {
        Log.d(TAG, "Analytics event is being prepared.");
        Bundle eventContentBundle = new Bundle();
        eventContentBundle.putInt(Constants.ANALYTICS_EVENT_BUNDLE_KEY_VENUE_ID, venue.getId());
        eventContentBundle.putString(Constants.ANALYTICS_EVENT_BUNDLE_KEY_VENUE_NAME, venue.getName());
        eventContentBundle.putString(Constants.ANALYTICS_EVENT_BUNDLE_KEY_VENUE_CATEGORY, venue.getVenueCategory().toString());
        CustomFunctions.postEventToAnalytics(eventName, eventContentBundle);
    }
}