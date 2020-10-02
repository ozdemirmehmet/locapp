/*
 * Created by Mehmet Ozdemir on 8/31/20 9:59 AM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 8/31/20 9:55 AM
 */

package com.likapalab.locapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.likapalab.locapp.R;
import com.likapalab.locapp.adapters.VenueInfoAdapter;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;

import java.util.ArrayList;

public class VenueMapFragment extends Fragment {

    //Map Constants
    private final float MAP_PARAMETER_VENUE_ZOOM_LEVEL = 14f;
    private final float MAP_PARAMETER_PIN_ALPHA = 0.7f;

    //Class Variables
    private VenueInfoAdapter venueInfoAdapter;
    private ArrayList<Venue> venueList;
    private ArrayList<Marker> markerList;
    private Marker focusedMarker;

    //Widgets
    private HuaweiMap huaweiMap;
    private MapView mapView;
    private ViewPager venueInfoViewPager;

    public VenueMapFragment() {
        // Required empty public constructor
    }

    public static VenueMapFragment newInstance(ArrayList<Venue> venueList) {
        VenueMapFragment fragment = new VenueMapFragment();
        fragment.venueList = venueList;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_venue_map, container, false);
        init(view, savedInstanceState);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(viewPagerHeightReceiver, new IntentFilter(Constants.INTENT_ACTION_VENUE_INFO_VIEW_PAGER_HEIGHT_RECEIVED));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        getActivity().unregisterReceiver(viewPagerHeightReceiver);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    private final BroadcastReceiver viewPagerHeightReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT)) {
                int viewPagerHeight = intent.getIntExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT, 0);
                if (venueInfoViewPager != null) {
                    venueInfoViewPager.getLayoutParams().height = viewPagerHeight;
                    venueInfoViewPager.requestLayout();
                }
            }
        }
    };

    private void init(View view, Bundle savedInstanceState) {
        ImageButton leftArrowImageView = view.findViewById(R.id.image_button_left_arrow);
        ImageButton rightArrowImageView = view.findViewById(R.id.image_button_right_arrow);

        leftArrowImageView.setOnClickListener(view1 -> {
            getItem(true);
        });

        rightArrowImageView.setOnClickListener(view1 -> {
            getItem(false);
        });

        venueInfoViewPager = view.findViewById(R.id.view_pager_venue_info);
        venueInfoViewPager.setPageMargin((int) getResources().getDimension(R.dimen.margin_container_large));

        mapView = view.findViewById(R.id.map_view);
        initMap(savedInstanceState);
    }

    private void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.BUNDLE_ARG_KEY_MAP_VIEW_API_KEY);
        }
        MapsInitializer.setApiKey(Constants.AGC_API_KEY);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(huaweiMap -> {
            this.huaweiMap = huaweiMap;

            huaweiMap.setOnMarkerClickListener(marker -> {
                int selectedMarkerPosition = (int) marker.getTag();
                focusMarker(selectedMarkerPosition);
                showItem(selectedMarkerPosition);
                return false;
            });

            markerList = new ArrayList<>();
            onDataSetChanged(venueList);
        });
    }

    private void onDataSetChanged(final ArrayList<Venue> venueList) {
        getActivity().runOnUiThread(() -> {
            markerList.clear();
            huaweiMap.clear();
            venueInfoViewPager.removeAllViews();

            for (int i = 0; i < venueList.size(); i++) {
                Venue venue = venueList.get(i);
                Marker marker = createMarker(venue);
                marker.setTag(i);
                markerList.add(marker);
            }

            if (venueList.size() > 0) {
                focusMarker(0);
            }

            venueInfoAdapter = new VenueInfoAdapter(getActivity().getSupportFragmentManager(), venueList);
            venueInfoViewPager.setAdapter(venueInfoAdapter);
            venueInfoAdapter.notifyDataSetChanged();
            venueInfoViewPager.clearOnPageChangeListeners();
            venueInfoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    focusMarker(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        });
    }

    private Marker createMarker(Venue venue) {
        int pinIconResourceId;
        switch (venue.getVenueCategory()) {
            case AIRPORT:
                pinIconResourceId = R.drawable.icon_pin_airport;
                break;
            case HOSPITAL:
                pinIconResourceId = R.drawable.icon_pin_hospital;
                break;
            case SCHOOL:
                pinIconResourceId = R.drawable.icon_pin_school;
                break;
            case SHOPPING:
                pinIconResourceId = R.drawable.icon_pin_shopping;
                break;
            default:
                pinIconResourceId = R.drawable.icon_pin_default;
        }
        return huaweiMap.addMarker(new MarkerOptions()
                .position(new LatLng(venue.getLatitude(), venue.getLongitude()))
                .title(venue.getName())
                .alpha(MAP_PARAMETER_PIN_ALPHA)
                .icon(CustomFunctions.getBitmapDescriptor(getActivity(), pinIconResourceId)));
    }

    private void focusMarker(int position) {
        Venue venue = venueList.get(position);
        LatLng coordinate = new LatLng(venue.getLatitude(), venue.getLongitude());
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinate, MAP_PARAMETER_VENUE_ZOOM_LEVEL);
        huaweiMap.animateCamera(location);

        if (focusedMarker != null) {
            focusedMarker.setAlpha(MAP_PARAMETER_PIN_ALPHA);
        }
        focusedMarker = markerList.get(position);
        focusedMarker.showInfoWindow();
        focusedMarker.setAlpha(1f);
    }

    private void getItem(boolean isLeft) {
        if (venueInfoViewPager != null && venueInfoViewPager.getAdapter() != null) {
            if (isLeft) {
                int index = venueInfoViewPager.getCurrentItem() - 1;
                if (index > -1 && index < venueInfoAdapter.getVenueList().size()) {
                    venueInfoViewPager.setCurrentItem(index, true);
                }
            } else {
                int index = venueInfoViewPager.getCurrentItem() + 1;
                if (index > -1 && index < venueInfoAdapter.getVenueList().size()) {
                    venueInfoViewPager.setCurrentItem(index, true);
                }
            }
        }
    }

    public void showItem(int position) {
        if (venueInfoViewPager != null && venueInfoViewPager.getAdapter() != null && position >= 0 && position < venueInfoViewPager.getAdapter().getCount()) {
            venueInfoViewPager.setCurrentItem(position, true);
        }
    }
}