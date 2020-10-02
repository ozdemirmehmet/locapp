/*
 * Created by Mehmet Ozdemir on 9/3/20 2:13 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/3/20 2:13 PM
 */

package com.likapalab.locapp.helpers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.huawei.hms.location.Geofence;
import com.huawei.hms.location.GeofenceRequest;
import com.huawei.hms.location.GeofenceService;
import com.huawei.hms.location.LocationServices;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.receivers.GeofenceBroadcastReceiver;

import java.util.ArrayList;

public class GeofenceHelper {

    //CLass Constants
    private static final String TAG = GeofenceHelper.class.getName();

    public static void initGeofences(Context context, ArrayList<Venue> venueList) {
        Log.d(TAG, "Geofences are being initialized.");
        ArrayList<Geofence> geofenceList = new ArrayList<>();
        for (int i = 0; i < venueList.size(); i++) {
            Venue venue = venueList.get(i);
            geofenceList.add(new Geofence.Builder()
                    .setUniqueId(String.valueOf(venue.getId()))
                    .setRoundArea(venue.getLatitude(), venue.getLongitude(), (float) venue.getGeofenceRadius())
                    .setValidContinueTime(Geofence.GEOFENCE_NEVER_EXPIRE)
                    .setConversions(Geofence.ENTER_GEOFENCE_CONVERSION | Geofence.EXIT_GEOFENCE_CONVERSION)
                    .build());
        }

        if (geofenceList.size() > 0) {
            addGeofences(context, geofenceList);
        } else {
            Log.d(TAG, "Venue not found to add geofence.");
        }
    }

    private static void addGeofences(Context context, ArrayList<Geofence> geofenceList) {
        GeofenceService geofenceService = LocationServices.getGeofenceService(context);
        geofenceService.createGeofenceList(getGeofencingRequest(geofenceList), getGeofencePendingIntent(context))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Geofences are created successfully.");
                    } else {
                        Log.d(TAG, "Geofences are not created. " + task.getException().getMessage());
                    }
                });
    }

    private static GeofenceRequest getGeofencingRequest(ArrayList<Geofence> geofenceList) {
        GeofenceRequest.Builder geofenceBuilder = new GeofenceRequest.Builder();
        geofenceBuilder.setInitConversions(0);
        geofenceBuilder.createGeofenceList(geofenceList);
        return geofenceBuilder.build();
    }

    private static PendingIntent getGeofencePendingIntent(Context context) {
        Intent geofenceIntent = new Intent(context, GeofenceBroadcastReceiver.class);
        PendingIntent geofencePendingIntents = PendingIntent.getBroadcast(context, 0, geofenceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntents;
    }
}
