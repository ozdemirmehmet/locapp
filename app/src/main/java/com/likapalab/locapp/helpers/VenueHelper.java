/*
 * Created by Mehmet Ozdemir on 9/2/20 9:43 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/2/20 9:43 AM
 */

package com.likapalab.locapp.helpers;

import android.content.Context;
import android.util.Log;

import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.enums.EVenueCategory;
import com.likapalab.locapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VenueHelper {

    //Class Constants
    private static final String TAG = VenueHelper.class.getName();

    public static void checkVenueList(Context context) {
        Log.d(TAG, "Check venue list request is received.");
        RemoteConfigurationHelper.getRemoteConfigurations(context);
    }

    public static ArrayList<Venue> getVenueList(Context context) {
        Log.d(TAG, "Venue list is being parsed.");
        String venueListString = (String) SharedPreferencesHelper.get(context, Constants.SHARED_PREFERENCES_KEY_VENUE_LIST, "");
        ArrayList<Venue> venueList = new ArrayList<>();
        try {
            JSONArray venueListJSONArray = new JSONArray(venueListString);
            for (int i = 0; i < venueListJSONArray.length(); i++) {
                JSONObject venueJSONObject = venueListJSONArray.getJSONObject(i);
                int id = venueJSONObject.getInt(Constants.JSON_OBJECT_KEY_VENUE_ID);
                String name = venueJSONObject.getString(Constants.JSON_OBJECT_KEY_VENUE_NAME);
                String description = venueJSONObject.getString(Constants.JSON_OBJECT_KEY_VENUE_DESCRIPTION);
                double latitude = venueJSONObject.getDouble(Constants.JSON_OBJECT_KEY_VENUE_LATITUDE);
                double longitude = venueJSONObject.getDouble(Constants.JSON_OBJECT_KEY_VENUE_LONGITUDE);
                double geofenceRadius = venueJSONObject.getDouble(Constants.JSON_OBJECT_KEY_VENUE_GEOFENCE_RADIUS);
                EVenueCategory category = EVenueCategory.valueOf(venueJSONObject.getString(Constants.JSON_OBJECT_KEY_VENUE_CATEGORY));
                venueList.add(new Venue(id, name, description, latitude, longitude, geofenceRadius, category));
            }
            Log.d(TAG, "Venue list parsing was completed successfully.");
        } catch (JSONException e) {
            Log.d(TAG, "Venue list parsing was failed. " + e.toString());
        }
        return venueList;
    }
}
