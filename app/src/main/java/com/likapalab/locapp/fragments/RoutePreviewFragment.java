/*
 * Created by Mehmet Ozdemir on 9/21/20 4:01 PM
 * Copyright (c) 2020 . All rights reserved.
 * Last modified 9/21/20 4:01 PM
 */

package com.likapalab.locapp.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.PolylineOptions;
import com.likapalab.locapp.R;
import com.likapalab.locapp.adapters.InstructionInfoAdapter;
import com.likapalab.locapp.models.entities.Bound;
import com.likapalab.locapp.models.entities.Path;
import com.likapalab.locapp.models.entities.Route;
import com.likapalab.locapp.models.entities.Venue;
import com.likapalab.locapp.utils.Constants;
import com.likapalab.locapp.utils.CustomFunctions;

public class RoutePreviewFragment extends Fragment {

    //Class Constants
    private static final int PATH_CAMERA_PADDING = 48;
    private static final int ROUTE_LINE_COLOR = Color.RED;
    private static final float ROUTE_LINE_WIDTH = 4f;
    private static final float ROUTE_STEP_CAMERA_TILT = 60f;
    private static final float ROUTE_STEP_CAMERA_ZOOM = 19f;
    private static final float ROUTE_CAMERA_ZOOM = 15f;

    //Widgets
    private MapView mapView;
    private ViewPager instructionInfoViewPager;

    //Variables
    private HuaweiMap huaweiMap;
    private Venue venue;
    private InstructionInfoAdapter instructionInfoAdapter;
    private Route route;

    public RoutePreviewFragment() {
        // Required empty public constructor
    }

    public static RoutePreviewFragment newInstance(Venue venue) {
        RoutePreviewFragment fragment = new RoutePreviewFragment();
        fragment.venue = venue;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_preview, container, false);
        init(view, savedInstanceState);
        return view;
    }

    private void init(View view, Bundle savedInstanceState) {
        ImageButton leftArrowImageView = view.findViewById(R.id.image_button_left_arrow);
        ImageButton rightArrowImageView = view.findViewById(R.id.image_button_right_arrow);

        leftArrowImageView.setOnClickListener(view1 -> {
            getItem(true);
        });

        rightArrowImageView.setOnClickListener(view1 -> {
            getItem(false);
        });

        instructionInfoViewPager = view.findViewById(R.id.view_pager_instruction_info);
        instructionInfoViewPager.setPageMargin((int) getResources().getDimension(R.dimen.margin_container_large));

        view.findViewById(R.id.card_view_route_preview).setOnClickListener(view1 -> {
            if (route != null) {
                animateCamera(route.getBounds());
            }
        });

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
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(routeReceiver, new IntentFilter(Constants.INTENT_ACTION_ROUTE_RECEIVED));
        getActivity().registerReceiver(viewPagerHeightReceiver, new IntentFilter(Constants.INTENT_ACTION_INSTRUCTION_INFO_VIEW_PAGER_HEIGHT_RECEIVED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        getActivity().unregisterReceiver(routeReceiver);
        getActivity().unregisterReceiver(viewPagerHeightReceiver);
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

    private final BroadcastReceiver routeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.INTENT_PARAMETER_ROUTE) && intent.hasExtra(Constants.INTENT_PARAMETER_START_LOCATION)) {
                Route route = (Route) intent.getSerializableExtra(Constants.INTENT_PARAMETER_ROUTE);
                com.likapalab.locapp.models.entities.LatLng startLocation = (com.likapalab.locapp.models.entities.LatLng) intent.getSerializableExtra(Constants.INTENT_PARAMETER_START_LOCATION);

                RoutePreviewFragment.this.route = route;
                addMarker(getString(R.string.text_start_location), startLocation.toHuaweiLatLng(), R.drawable.icon_blue_dot);
                createVenueMarker(venue);
                addRoutePolyLines(route);
                animateCamera(route.getBounds());
                prepareViewPager(route.getPaths().get(0));
            }
        }
    };

    private final BroadcastReceiver viewPagerHeightReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT)) {
                int viewPagerHeight = intent.getIntExtra(Constants.INTENT_PARAMETER_VIEW_PAGER_HEIGHT, 0);
                if (instructionInfoViewPager != null) {
                    instructionInfoViewPager.getLayoutParams().height = viewPagerHeight;
                    instructionInfoViewPager.requestLayout();
                }
            }
        }
    };

    private void addRoutePolyLines(Route route) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .color(ROUTE_LINE_COLOR)
                .width(ROUTE_LINE_WIDTH);

        Path path = route.getPaths().get(0);
        polylineOptions.add(new LatLng(path.getStartLocation().getLat(), path.getStartLocation().getLng()));
        for (int j = 0; j < path.getSteps().size(); j++) {
            for (int k = 0; k < path.getSteps().get(j).getPolyline().size(); k++) {
                polylineOptions.add(path.getSteps().get(j).getPolyline().get(k).toHuaweiLatLng());
            }
        }
        polylineOptions.add(new LatLng(path.getEndLocation().getLat(), path.getEndLocation().getLng()));

        huaweiMap.addPolyline(polylineOptions);
    }

    private void animateCamera(Bound bound) {
        CameraPosition stepCameraPosition = new CameraPosition.Builder()
                .bearing(0)
                .target(bound.getNortheast().toHuaweiLatLng())
                .tilt(0)
                .zoom(ROUTE_CAMERA_ZOOM)
                .build();
        animateCamera(stepCameraPosition);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(bound.getNortheast().toHuaweiLatLng());
        builder.include(bound.getSouthwest().toHuaweiLatLng());
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), PATH_CAMERA_PADDING));
    }

    private Marker createVenueMarker(Venue venue) {
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
        return addMarker(venue.getName(), new LatLng(venue.getLatitude(), venue.getLongitude()), pinIconResourceId);
    }

    private Marker addMarker(String name, LatLng location, int pinIconResourceId) {
        return huaweiMap.addMarker(new MarkerOptions()
                .position(location)
                .title(name)
                .icon(CustomFunctions.getBitmapDescriptor(getActivity(), pinIconResourceId)));
    }

    private void prepareViewPager(Path path) {
        instructionInfoAdapter = new InstructionInfoAdapter(getActivity().getSupportFragmentManager(), path.getSteps());
        instructionInfoViewPager.setAdapter(instructionInfoAdapter);
        instructionInfoAdapter.notifyDataSetChanged();
        instructionInfoViewPager.clearOnPageChangeListeners();
        instructionInfoViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                focusStep(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showInstruction(int position) {
        if (instructionInfoViewPager != null && instructionInfoViewPager.getAdapter() != null && position >= 0 && position < instructionInfoViewPager.getAdapter().getCount()) {
            instructionInfoViewPager.setCurrentItem(position, true);
        }
    }

    private void getItem(boolean isLeft) {
        if (instructionInfoViewPager != null && instructionInfoViewPager.getAdapter() != null) {
            if (isLeft) {
                int index = instructionInfoViewPager.getCurrentItem() - 1;
                if (index > -1 && index < instructionInfoAdapter.getStepList().size()) {
                    instructionInfoViewPager.setCurrentItem(index, true);
                }
            } else {
                int index = instructionInfoViewPager.getCurrentItem() + 1;
                if (index > -1 && index < instructionInfoAdapter.getStepList().size()) {
                    instructionInfoViewPager.setCurrentItem(index, true);
                }
            }
        }
    }

    private void focusStep(int position) {
        CameraPosition stepCameraPosition = new CameraPosition.Builder()
                .bearing(getDegrees(instructionInfoAdapter.getStepList().get(position).getPolyline().get(0).toHuaweiLatLng(), instructionInfoAdapter.getStepList().get(position).getPolyline().get(1).toHuaweiLatLng()))
                .target(instructionInfoAdapter.getStepList().get(position).getStartLocation().toHuaweiLatLng())
                .tilt(ROUTE_STEP_CAMERA_TILT)
                .zoom(ROUTE_STEP_CAMERA_ZOOM)
                .build();
        animateCamera(stepCameraPosition);
    }

    public static float getDegrees(LatLng source, LatLng destination) {
        double dLon = (destination.longitude - source.longitude);
        double y = Math.sin(dLon) * Math.cos(destination.latitude);
        double x = Math.cos(source.latitude) * Math.sin(destination.latitude) - Math.sin(source.latitude) * Math.cos(destination.latitude) * Math.cos(dLon);
        double bearing = Math.toDegrees((Math.atan2(y, x)));
        bearing = ((bearing + 360) % 360);
        return (float) bearing;
    }

    private void animateCamera(CameraPosition cameraPosition) {
        huaweiMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}