/*
 * Created by Mehmet Ozdemir on 9/2/20 9:13 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/2/20 9:13 AM
 */

package com.likapalab.locapp.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.likapalab.locapp.utils.Constants;

public class RemoteConfigurationHelper {

    //CLass Constants
    private static final String TAG = RemoteConfigurationHelper.class.getName();

    protected static void getRemoteConfigurations(Context context) {
        Log.d(TAG, "Remote configuration request is being started.");
        AGConnectConfig config = AGConnectConfig.getInstance();
        config.fetch(0).addOnSuccessListener(configValues -> {
            Log.d(TAG, "Remote configuration received successfully.");
            config.apply(configValues);
            long updateId = config.getValueAsLong(Constants.REMOTE_CONFIGURATION_KEY_UPDATE_ID);
            String venueList = config.getValueAsString(Constants.REMOTE_CONFIGURATION_KEY_VENUE_LIST);
            long lastUpdateId = (long) SharedPreferencesHelper.get(context, Constants.SHARED_PREFERENCES_KEY_UPDATE_ID, (long) 0);
            if (updateId > lastUpdateId) {
                Log.d(TAG, "Venue list needs to be updated.");
                SharedPreferencesHelper.put(context, Constants.SHARED_PREFERENCES_KEY_UPDATE_ID, updateId);
                SharedPreferencesHelper.put(context, Constants.SHARED_PREFERENCES_KEY_VENUE_LIST, venueList);

                broadcastVenueListReceived(context);

                GeofenceHelper.initGeofences(context, VenueHelper.getVenueList(context));
            } else {
                Log.d(TAG, "Venue list is already up to date.");
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "Remote configuration fetch failed. " + e.toString());
        });
    }

    private static void broadcastVenueListReceived(Context context) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_VENUE_LIST_RECEIVED);
        context.sendBroadcast(intent);
    }
}
