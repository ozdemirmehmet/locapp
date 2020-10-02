/*
 * Created by Mehmet Ozdemir on 9/21/20 3:55 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/21/20 3:55 PM
 */

package com.likapalab.locapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationAvailability;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.likapalab.locapp.R;
import com.likapalab.locapp.fragments.RouteInstructionListFragment;
import com.likapalab.locapp.fragments.RoutePreviewFragment;
import com.likapalab.locapp.models.entities.LatLng;
import com.likapalab.locapp.models.entities.Route;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.models.interfaces.IOnItemSelectListener;
import com.likapalab.locapp.models.request.DirectionRequest;
import com.likapalab.locapp.models.response.DirectionResponse;
import com.likapalab.locapp.services.ApiService;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;
import com.likapalab.locapp.widgets.LikapalabTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends AppCompatActivity {

    //Class Constants
    private final String TAG = RouteActivity.class.getName();

    //Class Variables
    private Venue venue;
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //Widgets
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        getVenue();

        CustomFunctions.customizeActionBar(this, getSupportActionBar(), R.string.text_route_preview);

        initViewPager();

        onProgress(true);
        initLocationElements();
    }

    private void getVenue() {
        venue = (Venue) getIntent().getSerializableExtra(Constants.INTENT_PARAMETER_VENUE);
        if (venue == null) {
            Toast.makeText(this, getString(R.string.text_toast_unexpected_error), Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    private void initViewPager() {
        RoutePreviewFragment routePreviewFragment = RoutePreviewFragment.newInstance(venue);
        RouteInstructionListFragment routeInstructionListFragment = RouteInstructionListFragment.newInstance(venue, (IOnItemSelectListener) (item, position) -> {
            if (viewPager != null && viewPager.getAdapter() != null) {
                viewPager.setCurrentItem(1, true);
                routePreviewFragment.showInstruction(position);
            }
        });

        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            private final Fragment[] fragments = new Fragment[]{
                    routeInstructionListFragment,
                    routePreviewFragment
            };
            private final String[] fragmentNames = new String[]{
                    getString(R.string.text_instructions),
                    getString(R.string.text_preview)
            };

            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames[position];
            }
        };

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            LikapalabTextView tabViewTextView = new LikapalabTextView(this);
            tabViewTextView.setText(tabLayout.getTabAt(i).getText());
            tabViewTextView.setTextColor(getResources().getColorStateList(R.color.color_style_tab_text));
            tabViewTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tabLayout.getTabAt(i).setCustomView(tabViewTextView);
        }
    }

    private void initLocationElements() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setInterval(0);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    List<Location> locations = locationResult.getLocations();
                    if (!locations.isEmpty()) {
                        for (Location location : locations) {
                            if (location.getAccuracy() < 15) {
                                if (currentLocation == null) {
                                    removeLocationUpdatesWithCallback();
                                    currentLocation = location;
                                    getDirections();
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (locationAvailability != null) {
                    boolean flag = locationAvailability.isLocationAvailable();
                    Log.d(TAG, "onLocationAvailability isLocationAvailable:" + flag);
                }
            }
        };

        requestLocationUpdatesWithCallback();
    }

    private void requestLocationUpdatesWithCallback() {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            settingsClient.checkLocationSettings(locationSettingsRequest)
                    .addOnSuccessListener(locationSettingsResponse -> {
                        Log.d(TAG, "Check location settings request is completed successfully.");
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "Request location updates is completed successfully.");
                                })
                                .addOnFailureListener(e -> {
                                    Log.d(TAG, "Request location updates is failed. Message:" + e.getMessage());
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Check location settings request is failed. Message:" + e.getMessage());
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(RouteActivity.this, 0);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.d(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                        }
                    });
        } catch (Exception e) {
            Log.d(TAG, "Request location updates is failed. Exception:" + e.getMessage());
        }
    }

    private void removeLocationUpdatesWithCallback() {
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Remove location updates request is completed successfully.");
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Remove location updates request is failed. Message: " + e.getMessage());
                    });
        } catch (Exception e) {
            Log.d(TAG, "Remove location updates request is failed. Exception:" + e.getMessage());
        }
    }

    private void getDirections() {
        DirectionRequest directionRequest = new DirectionRequest(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), new LatLng(venue.getLatitude(), venue.getLongitude()));
        ApiService.getHuaweiMapServices().getDirectionApiRequest("driving", Constants.AGC_API_KEY, directionRequest).enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                onProgress(false);
                if (response.body().getReturnCode().equals("0") && response.body().getReturnDesc().equals("OK")) {
                    broadcastRoute(response.body().getRoutes().get(0));
                } else {
                    Toast.makeText(RouteActivity.this, getString(R.string.text_toast_no_route), Toast.LENGTH_LONG).show();
                    RouteActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                onProgress(false);
                Log.d(TAG, "Direction request is failed. Message: " + t.getMessage());
                Toast.makeText(RouteActivity.this, getString(R.string.text_toast_unexpected_error), Toast.LENGTH_LONG).show();
                RouteActivity.this.finish();
            }
        });
    }

    private void onProgress(boolean isVisible) {
        findViewById(R.id.view_progress).setVisibility(isVisible ? View.VISIBLE : View.GONE);
        findViewById(R.id.view_progress_bar).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void broadcastRoute(Route route) {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_ACTION_ROUTE_RECEIVED);
        intent.putExtra(Constants.INTENT_PARAMETER_ROUTE, route);
        intent.putExtra(Constants.INTENT_PARAMETER_START_LOCATION, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        sendBroadcast(intent);
    }
}